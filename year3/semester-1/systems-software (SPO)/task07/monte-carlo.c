#include <math.h>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>

#include <pthread.h>

/** Defines how many times an attempt should be done. */
#define INTERVAL 1000000
/** The number of threads. */
#define NTHREADS 2

/** The mutex. */
pthread_mutex_t sum_mut;
/** Sum of all attempts. */
unsigned int Ns = 0;
/** Sum of successful attempts. */
unsigned int Zs = 0;

/** The thread arguments. */
struct arguments {
    unsigned int seed;
} args;
/** The thread pool. */
pthread_t thread_pool[NTHREADS];

void *approximate(void *arg);

int main(int argc, char *argv[]) {
    struct timespec time;
    clock_gettime(CLOCK_REALTIME, &time);

    for (int i = 0; i < NTHREADS; i++) {
        args.seed = time.tv_nsec;
        pthread_create(&thread_pool[i], NULL, approximate, &args);
    }

    for (int i = 0; i < NTHREADS; i++) {
        pthread_join(thread_pool[i], NULL);
    }

    pthread_mutex_destroy(&sum_mut);


    double PI = (4 * (double)Zs) / (double)Ns;
    printf("Zs: %i, Ns: %i\n", Zs, Ns);
    printf("Approximate PI: %f\n", PI);

    return 0;
}

/*
 * The function executed by the threads. It executes the Monte-Carlo algorithm,
 * meaning it simulates throwing dots into a circle, checking how many land and adding to
 * global sum.
 */
void *approximate(void *arg) {
    double x, y, distance;
    int N = 0;
    int Z = 0;
    struct arguments *args = (struct arguments *)arg;
    for (int i = 0; i < INTERVAL; i++) {
        x = ((double) rand_r(&args->seed)) / RAND_MAX;
        y = ((double) rand_r(&args->seed)) / RAND_MAX;

        distance = (x * x) + (y * y);

        if (distance <= 1) {
            Z++;
        }
        N++;

        pthread_mutex_lock(&sum_mut);
        if (Ns >= INTERVAL) {
            // If thread exits, unlock mutex so other threads can continue.
            pthread_mutex_unlock(&sum_mut);
            break;
        }
        Ns++;
        if (distance <= 1) {
            Zs++;
        }
        pthread_mutex_unlock(&sum_mut);
    }
}
