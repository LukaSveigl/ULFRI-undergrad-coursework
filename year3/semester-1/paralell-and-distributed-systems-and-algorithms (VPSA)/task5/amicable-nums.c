/*
 * File: amicable-nums.c
 * Description: Implementation of the multithreaded algorithm for calculating the sum of amicable 
 * numbers using OpenMP. The measurements of execution times will be appended to the end of this file.
 */

#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "omp.h"

/* 
 * Definitions for number of threads and array size. 
 * Modify to change parameters of execution.
 */
#define NTHREADS 1
#define ARRSIZE 100 * 1000

/** Array that stores the sum of divisors of number i at index i. */
int array[ARRSIZE];

// Compile: gcc -O2 -lm -fopenmp vpsa/vaja-5/amicable-nums.c -o vpsa/vaja-5/amicable_nums
// Run: vpsa/vaja-5/amicable_nums

int calculate_amicable(int n);

int main(int argc, char *argv[]) {
    omp_set_num_threads(NTHREADS);

    int sum = 0;
    double start = omp_get_wtime();

    // Depending on the macro definition passed during compilation,
    // activate specified schedule type.
    #ifdef M_STATIC 
        #pragma omp parallel for schedule(static, 100 * 1000)
    #elif M_DYNAMIC
        #pragma omp parallel for schedule(dynamic, 100 * 1000)
    #elif M_GUIDED
        #pragma omp parallel for schedule(guided, 100 * 1000)
    #elif M_AUTO
        #pragma omp parallel for schedule(auto)
    #endif
    for (int i = 1; i < ARRSIZE; i++) {
        array[i] = calculate_amicable(i);
    }

    // No need to schedule due to equal complexity.
    #ifdef M_STATIC 
        #pragma omp parallel for reduction(+:sum)
    #elif M_DYNAMIC
        #pragma omp parallel for reduction(+:sum)
    #elif M_GUIDED
        #pragma omp parallel for reduction(+:sum)
    #elif M_AUTO
        #pragma omp parallel for reduction(+:sum)
    #endif
    for (int i = 0; i < ARRSIZE; i++) {
        for (int j = 0; j < i; j++) {
            if (array[i] == j && array[j] == i) {
                sum += (i + j);
            }
        }
    }

    double end = omp_get_wtime();

    printf("Sum of amicable numbers in %i numbers: %i, time taken: %.5f\n", ARRSIZE, sum, end-start);

    return 0;
}

/*
 * Calculates the sum of proper divisors of number n. 
 */
int calculate_amicable(int n) {
    int sum = 1;
    for (int i = 2; i <= sqrt(n); i++) {
        if (n % i == 0) {
            sum += i;
            if (n / i != i) {
                sum += n / i;
            }
        }
    }
    return sum;
}

/*

Measurements for 100 000 numbers.

Ts = 10.04965s

1 thread:

static:
Tp = 7.90845s
Ts/Tp = 1.271

dynamic:
Tp = 5.99656s
Ts/Tp = 1.676

guided:
Tp = 6.00776s
Ts/Tp = 1.673

auto:
Tp = 7.89315s
Ts/Tp = 1.273

-------------------------

2 thread:

static:
Tp = 5.98160s
Ts/Tp = 1.680

dynamic:
Tp = 5.02724s
Ts/Tp = 1.999

guided:
Tp = 5.02546s
Ts/Tp = 2.000

auto:
Tp = 5.91734s
Ts/Tp = 1.698s

------------------------

4 thread:

static:
Tp = 3.55919s
Ts/Tp = 2.824

dynamic:
Tp = 3.34822s
Ts/Tp = 3.002

guided:
Tp = 2.72629s
Ts/Tp = 3.686

auto:
Tp = 3.46073s
Ts/Tp = 2.904

------------------------

8 thread:

static:
Tp = 2.00318s
Ts/Tp = 5.017

dynamic:
Tp = 1.84036s
Ts/Tp = 5.461

guided:
Tp = 1.80551s
Ts/Tp = 5.5661

auto:
Tp = 1.87004s
Ts/Tp = 5.374

------------------------

16 thread:

static:
Tp = 1.49386s
Ts/Tp = 6.727

dynamic:
Tp = 1.47300s
Ts/Tp = 6.823

guided:
Tp = 1.36727s
Ts/Tp = 7.350

auto:
Tp = 1.57454s
Ts/Tp = 6.383

------------------------

32 thread:

static:
Tp = 1.34092s
Ts/Tp = 7.495

dynamic:
Tp = 1.42194s
Ts/Tp = 7.068

guided:
Tp = 1.44811s
Ts/Tp = 6.940

auto:
Tp = 1.18450s
Ts/Tp = 6.770

------------------------

*/