/*
 * File: odd-even.cpp
 * Description: Implementation of the multithreaded odd-even sort on array of random numbers.
 * The measurements of execution times will be appended to the end of this file.
 */

#include <time.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

#include <pthread.h>

/* 
 * Definitions for number of threads, array size and max and min value. 
 * Modify to change parameters of execution.
 */
#define NTHREADS 8
#define ARRSIZE 12 * 10000
#define MINVALUE -10
#define MAXVALUE 40

/* Version of the algorithm to run: "rr" - round robin, "bl" - block. */
#define VER "bl"

/* The thread pool. */
pthread_t thread_pool[NTHREADS];
/* The thread barrier. */
pthread_barrier_t barrier;
/* The array to sort. It is global for ease of use. */
int array[ARRSIZE];
/* The flag that signifies if a swap occurred in the current iteration of the sort. */
bool swapped = false;

/* The thread arguments. */
struct arguments {
    int id;
} args[NTHREADS];

struct timespec timeStart, timeEnd;

void fill_array(int min, int max);
void print_array();
void swap(int idx1, int idx2);
void *odd_even(void *arg);
void *odd_even_b(void *arg);

int main(int argc, char **argv) {
    fill_array(MINVALUE, MAXVALUE);

    if (argc == 2 && strcmp(argv[1], "print") == 0) {
        print_array();
    }

    pthread_barrier_init(&barrier, NULL, NTHREADS);

    clock_t start = clock();
    clock_gettime(CLOCK_REALTIME, &timeStart);

    // Select appropriate version to run.
    if (strcmp(VER, "rr") == 0) {
        for (int i = 0; i < NTHREADS; i++) {
            args[i].id = i;
            pthread_create(&thread_pool[i], NULL, odd_even, (void *) &args[i]);
        }
        for (int i = 0; i < NTHREADS; i++) {
            pthread_join(thread_pool[i], NULL);
        }
    }
    else if (strcmp(VER, "bl") == 0) {
        for (int i = 0; i < NTHREADS; i++) {
            args[i].id = i;
            pthread_create(&thread_pool[i], NULL, odd_even_b, (void *) &args[i]);
        }
        for (int i = 0; i < NTHREADS; i++) {
            pthread_join(thread_pool[i], NULL);
        }
    }

    // Calculate times.
    clock_gettime(CLOCK_REALTIME, &timeEnd);
    clock_t end = clock();
    double time_taken = ((double)(end-start))/CLOCKS_PER_SEC; // calculate the elapsed time
    
    double elapsed_time = (timeEnd.tv_sec - timeStart.tv_sec) + (timeEnd.tv_nsec - timeStart.tv_nsec) / 1e9;    // in seconds 
    printf("Čas izvajanja: %f sekund \n", time_taken);
    printf("Čas izvajanja: %f sekund \n", elapsed_time);

    if (argc == 2 && strcmp(argv[1], "print") == 0) {
        print_array();
    }

#ifdef DBG
    // Validate the result array.
    for(int i = 1; i < ARRSIZE; i++){ 
        if(array[i-1] > array[i]){
            printf("Error at index %i\n", i - 1);
            printf("Array[i - 1] : %i\n", array[i - 1]);
            printf("Array[i] : %i\n", array[i]);
            break;
        }
    }
#endif

    return 0;
}

/*
 * The thread function to execute the odd-even sort based on the round 
 * robin principle, meaning for example: t1 compares indices 1,2, t2 compares 3,4,
 * t1 compares 5,6, etc.
 */
void *odd_even(void *arg) {
    struct arguments *args = (arguments *)arg;
    int id = args->id;

#ifdef DBG
    printf("Thread %i started execution.\n  Required jumps: %i\n", id, 
    (((ARRSIZE - 1) - (1 + (2 * id))) / (2 * NTHREADS)) + 1);
#endif

    int diff = 2 * NTHREADS;

    for (int i = 0; i < (ARRSIZE / 2) + 1; i++) {  
        swapped = false;
        for (int j = (2 + (2 * id)); j < ARRSIZE; j += diff) {
            if (array[j] > array[j - 1]) {
                swap(j, j - 1);
                swapped = true;
            }     
        }
        // Synchronise after odd phase.
        pthread_barrier_wait(&barrier); 
        for (int j = (1 + (2 * id)); j < ARRSIZE; j += diff) {
            if (array[j] > array[j - 1]) {
                swap(j, j - 1);
                swapped = true;
            }
        }
        // Synchronise after even phase.
        pthread_barrier_wait(&barrier);
        // "Optimization" - execution stops when no swaps were completed in the current 
        // iteration of the program, although this makes it slower.
        if (!swapped) { 
            break;
        }
        else {
            pthread_barrier_wait(&barrier);
        }
    }

    return NULL;
}

/*
 * The thread function to execute the odd-even sort based on the block
 * principle, meaning each thread resolves (id + 1) * ARRSIZE / NTHREADS - (id * ARRSIZE) / NTHREADS
 * consecutive pairs.
 */
void *odd_even_b(void *arg) {
    arguments *args = (arguments *)arg;
    int id = args->id;

    int block = (((id + 1) * ARRSIZE ) / NTHREADS - (id * ARRSIZE) / NTHREADS);
    int start = id == 0 ? 1 : (id * ARRSIZE) / NTHREADS;

#ifdef DBG
    printf("Thread %i:\n    Start: %i, size: %i\n", id, start, block);
#endif

    for (int i = 0; i < (ARRSIZE / 2) + 1; i++) {
        swapped = false;
        for (int j = start + 1; j < start + block; j += 2) {
            if (array[j - 1] > array[j]) {
                swap(j, j - 1);
                swapped = true;
            }
        }
        // Synchronise after odd phase.
        pthread_barrier_wait(&barrier);
        for (int j = start; j < start + block - 1; j += 2) {
            if (array[j - 1] > array[j]) {
                swap(j, j - 1);
                swapped = true;
            }
        }
        // Synchronise after even phase.
        pthread_barrier_wait(&barrier);
        // "Optimization" - execution stops when no swaps were completed in the current 
        // iteration of the program, although this makes it slower.
        if (!swapped) {
            break;
        }
        else {
            pthread_barrier_wait(&barrier);
        }
    }

    return NULL;
}

/*
 * Swaps the elements in global array at the given indices.
 */
void swap(int idx1, int idx2) {
    int tmp = array[idx1];
    array[idx1] = array[idx2];
    array[idx2] = tmp;
    swapped = true;
}

/*
 * Prints the global array.
 */
void print_array() {
    printf("{ ");
    for (int i = 0; i < ARRSIZE; i++) {
        printf("%i ", array[i]);
    }
    printf("}\n");
}

/*
 * Fills the global array with random integer values specified by 
 * the min and max parameters.
 */
void fill_array(int min, int max) {
    srand(time(NULL));
    for (int i = 0; i < ARRSIZE; i++) {
        array[i] = rand() % (max - min + 1) + min;
    }
}

/*

Execution times for round-robin:

+----------+-----------+-------------+--------------+
| NTHREADS | ARRSIZE   | Time taken  | Elapsed time |
+----------+-----------+-------------+--------------+
| 1        | 1.2*10^5  | 16.249510s  | 16.371484s   |
+----------+-----------+-------------+--------------+
| 2        | 1.2*10^5  | 36.957798s  | 20.086342s   |
+----------+-----------+-------------+--------------+
| 4        | 1.2*10^5  | 71.313298s  | 20.474355s   |
+----------+-----------+-------------+--------------+
| 8        | 1.2*10^5  | 210.185526s | 35.272151s   |
+----------+-----------+-------------+--------------+

Execution times for block:

+----------+-----------+-------------+--------------+
| NTHREADS | ARRSIZE   | Time taken  | Elapsed time |
+----------+-----------+-------------+--------------+
| 1        | 1.2*10^5  | 17.390535s  | 17.514078s   |
+----------+-----------+-------------+--------------+
| 2        | 1.2*10^5  | 25.870892s  | 13.662771s   |
+----------+-----------+-------------+--------------+
| 4        | 1.2*10^5  | 30.550298s  | 10.086792s   |
+----------+-----------+-------------+--------------+
| 8        | 1.2*10^5  | 43.602060s  | 9.982701s    |
+----------+-----------+-------------+--------------+

* Do note that these are one time measurements, a more accurate representation
would be measuring each execution multiple times and averaging it. *

As visible above, the block implementation of the odd-even algorithm
performs drastically better than the round-robin implementation. This
could be a quirk of the way the algorithm works, or it could be a bad
implementation on my part.

Also interesting is the fact that the program takes more time to finish
with multiple threads ("time taken" column), which I suspect is due to
the overhead of managing multiple threads.

*/