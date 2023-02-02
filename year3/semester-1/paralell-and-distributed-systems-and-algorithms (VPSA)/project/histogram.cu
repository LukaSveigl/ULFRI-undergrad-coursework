#include <stdlib.h>
#include <math.h>
#define STB_IMAGE_IMPLEMENTATION
#define STB_IMAGE_WRITE_IMPLEMENTATION

#include "stb_image.h"
#include "stb_image_write.h"

#include <cuda_runtime.h>
#include <cuda.h>
#include "helper_cuda.h"

/** The number of grayscale values in the image. */
#define GRAYLEVELS 256
/** The number of desired channels in image. */
#define DESIRED_NCHANNELS 1
/** The size of a CUDA block. */
#define BLOCK_SIZE 256

// UTILITY FUNCTIONS

/**
 * Finds the minimum in the cumulative distribution function.
 * @param cdf - The cumulative distribution function.
 * @return - The minimum.
 */
unsigned long findMin(unsigned int *cdf) {
    unsigned long min = 0;
    for (int i = 0; min == 0 && i < GRAYLEVELS; i++) {
		min = cdf[i];
    }
    return min;
}

/**
 * Scales the cumulative distribution function.
 * @param cdf       - The cumulative distribution function.
 * @param cdfmin    - The cumulative distribution function minimum.
 * @param imageSize - The image size.
 * @return - The scale.
 */
__device__ inline unsigned char scale(unsigned long cdf, unsigned long cdfmin, unsigned long imageSize) {
    float scale;
    scale = (float)(cdf - cdfmin) / (float)(imageSize - cdfmin);
    scale = round(scale * (float)(GRAYLEVELS-1));
    return (int)scale;
}

// HISTOGRAM EQUALIZATION ALGORITHM FUNCTIONS

/**
 * Creates the histogram for the input grayscale image.
 * @param image     - The grayscale image.
 * @param width     - The image width.
 * @param height    - The image height.
 * @param histogram - The computed histogram.
 */
__global__ void CalculateHistogram(unsigned char *image, int width, int height, unsigned int *histogram) { 
    __shared__ unsigned int shared_cache[GRAYLEVELS];

    // Clear the shared cache.
    if (threadIdx.x < GRAYLEVELS) {
        shared_cache[threadIdx.x] = 0;
    }

    __syncthreads();

    int threadId = threadIdx.x + blockDim.x * blockIdx.x;
    int stride = blockDim.x * gridDim.x;

    while (threadId < width * height) {
        // Atomic operations must be employed because the threads access
        // the same memory.
        atomicAdd(&(shared_cache[(int)image[threadId]]), 1);
        threadId += stride;
    }

    __syncthreads();

    if (threadIdx.x < GRAYLEVELS) {
        atomicAdd(&(histogram[threadIdx.x]), shared_cache[threadIdx.x]);
    }
}

/**
 * Calculates the cumulative distribution histogram using the Work-Efficient Sum Scan implementation provided
 * here: https://developer.nvidia.com/gpugems/gpugems3/part-vi-gpu-computing/chapter-39-parallel-prefix-sum-scan-cuda
 * @param histogram - The histogram.
 * @param cdf       - The cumulative distribution function.
 */
__global__ void CalculateCDF(unsigned int *histogram, unsigned int *cdf, int width, int height) {
    extern __shared__ float scan[2 * GRAYLEVELS];

    int threadId = threadIdx.x;
    int offset = 1;

    scan[2 * threadId] = histogram[2 * threadId];
    scan[2 * threadId + 1] = histogram[2 * threadId + 1];

    // Build sum in place.
    for (int d = GRAYLEVELS >> 1; d > 0; d >>= 1) {
        __syncthreads();

        if (threadId < d) {
            int ai = offset * (2 * threadId + 1) - 1;
            int bi = offset * (2 * threadId + 2) - 1;
            scan[bi] += scan[ai];
        }

        offset *= 2;
    }

    if (threadId == 0) {
        scan[GRAYLEVELS - 1] = 0;
    }

    // Traverse down tree and build scan.
    for (int d = 1; d < GRAYLEVELS; d *= 2) {
        offset >>= 1;
        __syncthreads();

        if (threadId < d) {
            int ai = offset * (2 * threadId + 1) - 1;
            int bi = offset * (2 * threadId + 2) - 1;

            float t = scan[ai];
            scan[ai] = scan[bi];
            scan[bi] += t;
        }
    }
    __syncthreads();

    // Store results of scan into cumulative distribution function.
    cdf[2 * threadId] = scan[2 * threadId];
    cdf[2 * threadId + 1] = scan[2 * threadId + 1];
}

/**
 * Equalizes the image with the histogram.
 * @param image_in  - The input image.
 * @param image_out - The output image.
 * @param width     - The image width.
 * @param height    - The image height.
 * @param cdf       - The cumulative distribution function.
 * @param cdfmin    - The minimum in the cumulative distribution function.
 */
__global__ void Equalize(unsigned char *image_in, unsigned char *image_out, int width, int height, unsigned int* cdf, unsigned long cdfmin) {
    unsigned long imageSize = width * height;
    //unsigned long cdfmin = findMin(cdf);

    int threadId = threadIdx.x + blockIdx.x * blockDim.x;
    int stride = stride = blockDim.x * gridDim.x;

    while (threadId < imageSize) {
        image_out[threadId] = scale(cdf[(int)image_in[threadId]], cdfmin, imageSize);
        threadId += stride;
    }
    __syncthreads();
}


int main(int argc, char *argv[]) {
    if (argc < 3) {
        printf("USAGE: %s input_image output_image\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    char szImage_in_name[255];
    char szImage_out_name[255];

    snprintf(szImage_in_name, 255, "%s", argv[1]);
    snprintf(szImage_out_name, 255, "%s", argv[2]);


	//
    // Read image from file.
    //

    int width, height, cpp;
    // Read only DESIRED_NCHANNELS channels from the input image.
    unsigned char *imageIn = stbi_load(szImage_in_name, &width, &height, &cpp, DESIRED_NCHANNELS);
    if(imageIn == NULL) {
        printf("Error in loading the image\n");
        return 1;
    }
    const size_t datasize = height * width * cpp * sizeof(unsigned long);
    printf("Loaded image W = %d, H = %d, actual cpp = %d \n", width, height, cpp);
    
    int imageSize = width * height * cpp;
	//unsigned char *imageOut = (unsigned char *)malloc(height * width * sizeof(unsigned long));
    unsigned char *imageOut = (unsigned char *)malloc(datasize);



    //
    // Setup device variables.
    //

    unsigned char *d_imageIn;
    unsigned char *d_imageOut;
    unsigned int *d_histogram;
    unsigned int *d_CDF;

    // Prepare memory for device. This entails allocating memory on the device, copying read
    // image to device, etc.
    checkCudaErrors(cudaMalloc(&d_imageIn, width * height * sizeof(unsigned char)));
    checkCudaErrors(cudaMalloc(&d_imageOut, datasize));
    checkCudaErrors(cudaMemcpy(d_imageIn, imageIn, width * height * sizeof(unsigned char), cudaMemcpyHostToDevice));

    checkCudaErrors(cudaMalloc(&d_histogram, GRAYLEVELS * sizeof(unsigned int)));
    checkCudaErrors(cudaMalloc(&d_CDF, GRAYLEVELS * sizeof(unsigned int)));

    // Set histogram and cdf to 0, would be better if it was done parallel.
    checkCudaErrors(cudaMemset(d_histogram, 0, GRAYLEVELS * sizeof(unsigned int)));
    checkCudaErrors(cudaMemset(d_CDF, 0, GRAYLEVELS * sizeof(unsigned int)));

    // Prepare CUDA events for time measurement.
    cudaEvent_t start, stop;
    cudaEventCreate(&start);
    cudaEventCreate(&stop);
    cudaEventRecord(start);

    // Calculate the grid and block sizes.    
    dim3 blockSize(BLOCK_SIZE);
    dim3 gridSize(ceil(imageSize) / BLOCK_SIZE);


    //
    // Execute kernel.
    //

	// Create the histogram of grayscale values for the given image.
	CalculateHistogram<<<gridSize, blockSize>>>(d_imageIn, width, height, d_histogram);

    #ifdef DEBUG_HIST
        unsigned int *debug_hist = (unsigned int *)malloc(GRAYLEVELS * sizeof(unsigned int));
        checkCudaErrors(cudaMemcpy(debug_hist, d_histogram, GRAYLEVELS * sizeof(unsigned int), cudaMemcpyDeviceToHost));

        for (int i = 0; i < GRAYLEVELS; i++) {
            printf("hist[%i]=%i\n", i, debug_hist[i]);
        }
    #endif

	// Calculate the cumulative distribution histogram using the Work-Efficient Sum Scan.
	CalculateCDF<<<1, GRAYLEVELS>>>(d_histogram, d_CDF, width, height);

    #ifdef DEBUG_CDF
        unsigned int *debug_cdf = (unsigned int *)malloc(GRAYLEVELS * sizeof(unsigned int));
        checkCudaErrors(cudaMemcpy(debug_cdf, d_CDF, GRAYLEVELS * sizeof(unsigned int), cudaMemcpyDeviceToHost));

        for (int i = 0; i < GRAYLEVELS; i++) {
            printf("cdf[%i]=%i\n", i, debug_cdf[i]);
        }
    #endif

    // Compute the min in CDF on the CPU, as it only needs to be computed once. 
    unsigned int *CDF = (unsigned int*)malloc(GRAYLEVELS * sizeof(unsigned int));
    checkCudaErrors(cudaMemcpy(CDF, d_CDF, GRAYLEVELS * sizeof(unsigned int), cudaMemcpyDeviceToHost));
    unsigned long cdfmin = findMin(CDF);
    free(CDF);

	// Equalize the image using the calculated cumulative distribution function.
	Equalize<<<gridSize, blockSize>>>(d_imageIn, d_imageOut, width, height, d_CDF, cdfmin);

    //
    // Kernel finished.
    //


    checkCudaErrors(cudaMemcpy(imageOut, d_imageOut, datasize, cudaMemcpyDeviceToHost));

    cudaEventRecord(stop);
    cudaEventSynchronize(stop);

    float milliseconds = 0;
    cudaEventElapsedTime(&milliseconds, start, stop);
    printf("Kernel Execution time is: %0.3f milliseconds \n", milliseconds);


    //
    // Save image to file.
    //

    // Retrieve output file type.
    char szImage_out_name_temp[255];
    strncpy(szImage_out_name_temp, szImage_out_name, 255);
    char *token = strtok(szImage_out_name_temp, ".");
    char *FileType = NULL;

    while (token != NULL) {
        FileType = token;
        token = strtok(NULL, ".");
    }

    // Write output image to file.
    if (!strcmp(FileType, "png"))
        stbi_write_png(szImage_out_name, width, height, cpp, imageOut, width * cpp);
    else if (!strcmp(FileType, "jpg"))
        stbi_write_jpg(szImage_out_name, width, height, cpp, imageOut, 100);
    else if (!strcmp(FileType, "bmp"))
        stbi_write_bmp(szImage_out_name, width, height, cpp, imageOut);
    else
        printf("Error: Unknown image format %s! Only png, bmp, or bmp supported.\n", FileType);

    cudaEventDestroy(start);
	cudaEventDestroy(stop);


    /*
     * Free device and CPU memory.
     */

    checkCudaErrors(cudaFree(d_imageIn));
    checkCudaErrors(cudaFree(d_imageOut));
    checkCudaErrors(cudaFree(d_histogram));
    checkCudaErrors(cudaFree(d_CDF));

	free(imageIn);
    free(imageOut);

	return 0;
}



