//
//  Created by Patricio Bulic, Davor Sluga, UL FRI on 6/6/2022.
//  Copyright © 2022 Patricio Bulic, Davor Sluga UL FRI. All rights reserved.
//

#include <time.h>
#include <stdio.h>
#include <stdlib.h>

#include <cuda_runtime.h>
#include <cuda.h>
#include "helper_cuda.h"

#define STB_IMAGE_IMPLEMENTATION
#define STB_IMAGE_WRITE_IMPLEMENTATION
#include "stb_image.h"
#include "stb_image_write.h"

#define COLOR_CHANNELS 4
#define BLOCK_SIZE 16
//#define CHUNK_SIZE (BLOCK_SIZE + 2)

//***************************************************
// Image sharpening using a 3x3 kernel; 
// Source: https://setosa.io/ev/image-kernels/
//
//      |  0  -1   0 |
// K =  | -1   5  -1 |
//      |  0  -1   0 |
//
//***************************************************

__device__ inline unsigned char getIntensity(const unsigned char *image, int row, int col,
                                             int channel, int height, int width, int cpp)
{
    if (col < 0 || col >= width)
        return 0;
    if (row < 0 || row >= height)
        return 0;
    return image[(row * width + col) * cpp + channel];
}


// CUDA kernel for image sharpening. Each thread computes one output pixel
__global__ void sharpen(const unsigned char *imageIn, unsigned char *imageOut, const int width, const int height, const int cpp)
{
    // Get pixel
    int x = blockIdx.x * blockDim.x + threadIdx.x;
    int y = blockIdx.y * blockDim.y + threadIdx.y;

    if (x < width && y < height)
    {
        for (int c = 0; c < cpp; c++)
        {
            unsigned char px01 = getIntensity(imageIn, y - 1, x, c, height, width, cpp);
            unsigned char px10 = getIntensity(imageIn, y, x - 1, c, height, width, cpp);
            unsigned char px11 = getIntensity(imageIn, y, x, c, height, width, cpp);
            unsigned char px12 = getIntensity(imageIn, y, x + 1, c, height, width, cpp);
            unsigned char px21 = getIntensity(imageIn, y + 1, x, c, height, width, cpp);

            short pxOut = (5 * px11 - px01 - px10 - px12 - px21);
            pxOut = MIN(pxOut, 255);
            pxOut = MAX(pxOut, 0);
            imageOut[(y * width + x) * cpp + c] = (unsigned char)pxOut;
        }
    }
}


/**
 * Gets intensity for the serial algorithm.
 */
unsigned char getIntensitySingle(const unsigned char *image, int row, int col,
                                             int channel, int height, int width, int cpp)
{
    if (col < 0 || col >= width)
        return 0;
    if (row < 0 || row >= height)
        return 0;
    return image[(row * width + col) * cpp + channel];
}

/**
 * Serially executes the sharpening algorithm.
 */
void sharpenSingle(const unsigned char *imageIn, unsigned char *imageOut, const int width, const int height, const int cpp) {
    for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
            for (int c = 0; c < cpp; c++)
            {
                unsigned char px01 = getIntensitySingle(imageIn, y - 1, x, c, height, width, cpp);
                unsigned char px10 = getIntensitySingle(imageIn, y, x - 1, c, height, width, cpp);
                unsigned char px11 = getIntensitySingle(imageIn, y, x, c, height, width, cpp);
                unsigned char px12 = getIntensitySingle(imageIn, y, x + 1, c, height, width, cpp);
                unsigned char px21 = getIntensitySingle(imageIn, y + 1, x, c, height, width, cpp);
                short pxOut = (5 * px11 - px01 - px10 - px12 - px21);
                pxOut = MIN(pxOut, 255);
                pxOut = MAX(pxOut, 0);
                imageOut[(y * width + x) * cpp + c] = (unsigned char)pxOut;
            }
        }
    }
}

int main(int argc, char *argv[])
{

    if (argc < 3)
    {
        printf("USAGE: prog input_image output_image\n");
        exit(EXIT_FAILURE);
    }
    
    char szImage_in_name[255];
    char szImage_out_name[255];

    snprintf(szImage_in_name, 255, "%s", argv[1]);
    snprintf(szImage_out_name, 255, "%s", argv[2]);

    // Load image from file and allocate space for the output image
    int width, height, cpp;
    unsigned char *h_imageIn = stbi_load(szImage_in_name, &width, &height, &cpp, COLOR_CHANNELS);
    cpp = COLOR_CHANNELS;

    if (h_imageIn == NULL)
    {
        printf("Error reading loading image %s!\n", szImage_in_name);
        exit(EXIT_FAILURE);
    }
    printf("Loaded image %s of size %dx%d.\n", szImage_in_name, width, height);
    const size_t datasize = width * height * cpp * sizeof(unsigned char);
    unsigned char *h_imageOut = (unsigned char *)malloc(datasize);

    dim3 blockSize(BLOCK_SIZE, BLOCK_SIZE);
    dim3 gridSize(ceil(width / blockSize.x), ceil(height / blockSize.y));

    unsigned char *d_imageIn;
    unsigned char *d_imageOut;



    /*
        Serial algorithm.
    */

    struct timespec timeStart, timeEnd;
    clock_t start_time = clock();
    clock_gettime(CLOCK_REALTIME, &timeStart);

    unsigned char *d_imageInSingle = (unsigned char *)malloc(datasize);
    memcpy(d_imageInSingle, h_imageIn, datasize);
    unsigned char *d_imageOutSingle = (unsigned char *)malloc(datasize);

    sharpenSingle(d_imageInSingle, d_imageOutSingle, width, height, cpp);

    clock_gettime(CLOCK_REALTIME, &timeEnd);
    clock_t end_time = clock();
    double time_taken = ((double)(end_time-start_time))/CLOCKS_PER_SEC; // calculate the elapsed time
    
    double elapsed_time = (timeEnd.tv_sec - timeStart.tv_sec) + (timeEnd.tv_nsec - timeStart.tv_nsec) / 1e9;    // in seconds 
    printf("Serial Execution time is: %0.3f milliseconds \n", time_taken * 1000);
    printf("Serial Execution time is: %0.3f milliseconds \n", elapsed_time * 1000);

    free(d_imageInSingle);
    free(d_imageOutSingle);

    /*
        End of serial algorithm.
    */



    // Allocate device memory for images
    checkCudaErrors(cudaMalloc(&d_imageIn, datasize));
    checkCudaErrors(cudaMalloc(&d_imageOut, datasize));

    // Copy input image to device
    checkCudaErrors(cudaMemcpy(d_imageIn, h_imageIn, datasize, cudaMemcpyHostToDevice));

    // Create CUDA events
    cudaEvent_t start, stop;
    cudaEventCreate(&start);
    cudaEventCreate(&stop);

    // Execute the kernel
    cudaEventRecord(start);
    sharpen<<<gridSize, blockSize>>>(d_imageIn, d_imageOut, width, height, cpp);
    getLastCudaError("sharpen() execution failed\n");
    cudaEventRecord(stop);

    // Copy image back to host
    checkCudaErrors(cudaMemcpy(h_imageOut, d_imageOut, datasize, cudaMemcpyDeviceToHost));

    // Wait for the event to finish
    cudaEventSynchronize(stop);

    float milliseconds = 0;
    cudaEventElapsedTime(&milliseconds, start, stop);
    printf("Kernel Execution time is: %0.3f milliseconds \n", milliseconds);

    // Retrieve output file type
    char szImage_out_name_temp[255];
    strncpy(szImage_out_name_temp, szImage_out_name, 255);
    char *token = strtok(szImage_out_name_temp, ".");
    char *FileType = NULL;
    while (token != NULL)
    {
        FileType = token;
        token = strtok(NULL, ".");
    }
    // Write output image to file
    if (!strcmp(FileType, "png"))
        stbi_write_png(szImage_out_name, width, height, cpp, h_imageOut, width * cpp);
    else if (!strcmp(FileType, "jpg"))
        stbi_write_jpg(szImage_out_name, width, height, cpp, h_imageOut, 100);
    else if (!strcmp(FileType, "bmp"))
        stbi_write_bmp(szImage_out_name, width, height, cpp, h_imageOut);
    else
        printf("Error: Unknown image format %s! Only png, bmp, or bmp supported.\n", FileType);

    // Release device memory
    checkCudaErrors(cudaFree(d_imageIn));
    checkCudaErrors(cudaFree(d_imageOut));

    // Clean up the two events
	cudaEventDestroy(start);
	cudaEventDestroy(stop);
    
    // Release host memory
    free(h_imageIn);
    free(h_imageOut);

    return 0;
}

/*

Execution times:

+-----------+------------+-------------+--------------+
| IMGSIZE   | Tcpu       | Tgpu        | S            |
+-----------+------------+-------------+--------------+
| 640x480   | 52.542ms   | 0.176ms     | 298.534      |
+-----------+------------+-------------+--------------+
| 800x600   | 79.712ms   | 0.235ms     | 339.200      |
+-----------+------------+-------------+--------------+
| 1600x900  | 261.934ms  | 0.559ms     | 468.576      |
+-----------+------------+-------------+--------------+
| 1920x1080 | 375.542ms  | 0.776ms     | 483.946      |
+-----------+------------+-------------+--------------+
| 3840x2160 | 1535.421ms | 2.935ms     | 523.142      |
+-----------+------------+-------------+--------------+


*/
