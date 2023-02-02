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
 * Writes the process ID to the shared space.
 */
void write_to_shared_space() {
    // Create semaphore.
    key_t key_sem = ftok("shared_sem_file", 'a');
    int semid = semget(key_sem, 1, 0666 | IPC_CREAT);
    if (semid == -1) {
        internal_err("Could not create semaphore\n");
    }

    // Create shared memory and write to location.
    key_t key_shm = ftok("shared_mem_file", 'A');
    int shmid = shmget(key_shm, MSIZE, 0666 | IPC_CREAT);
    if (shmid == -1) {
        printf("%i, key %i\n", errno, key_shm);
        internal_err("Could not create shared memory\n")
    }

    // Lock semaphore.
    semun arg;
    arg.val = 0;

    if (semctl(semid, 0, SETVAL, arg) < 0) {
        internal_err("semctl lock\n");
    }

    char *str = shmat(shmid, NULL, 0);
    if (*str == -1) {
        internal_err("Could not attach to memory location\n");
    }
    strcpy(str, "pid = ");
    char pid[50];
    sprintf(pid, "%d", getpid());
    printf("PID: %s\n", pid);
    strcat(str, pid);

    if (shmdt(str) == -1) {
        internal_err("Could not detach from memory location\n");
    }

    // Wait for 10 seconds before unlocking semaphore.
    sleep(10);

    arg.val = 1;
    if (semctl(semid, 0, SETVAL, arg) < 0) {
        internal_err("semctl unlock\n");
    }
}

int main(int argc, char *argv[]) {
    write_to_shared_space();
    return 0;
}
