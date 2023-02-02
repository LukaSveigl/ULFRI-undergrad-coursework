#include <stdio.h>
#include <fcntl.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/sem.h>

#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }

/* Max memory space the program can access. */
#define MSIZE 1024

/* The semaphore union. */
typedef union semun {
    int val;
    struct semid_ds *buf;
    unsigned short *array;
} semun;

/*
 * Reads data from shared space.
 */
void read_from_shared_space() {
    // Get semaphore.
    key_t key_sem = ftok("shared_sem_file", 'a');
    int semid = semget(key_sem, 1, 0666);
    if (semid == -1) {
        internal_err("Could not get semaphore\n");
    }

    // Get shared memory.
    key_t key_shm = ftok("shared_mem_file", 'A');
    int shmid = shmget(key_shm, MSIZE, 0666);
    if (shmid == -1) {
        printf("%i, key %i\n", errno, key_shm);
        internal_err("Could not get shared memory\n")
    }

    semun arg;
    arg.val = 0;

    // Wait until semaphore unlocked.
    while (semctl(semid, 0, GETVAL, arg) != 1) {
        // Do nothing, just wait for the semaphore to unlock.
    }

    char *str = shmat(shmid, NULL, 0);
    if (*str == -1) {
        internal_err("Could not attach to memory location\n");
    }

    printf("Value at shared mem space: %s\n", str);

    if (shmdt(str) == -1) {
        internal_err("Could not detach from memory location\n");
    }
}

int main(int argc, char *argv[]) {
    read_from_shared_space();
    return 0;
}
