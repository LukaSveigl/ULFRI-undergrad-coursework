#include <stdio.h>
#include <fcntl.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/msg.h>

#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }

/* Max file size the program can accept. */
#define MAXFSIZE 16 * 1024

/* The message format. */
typedef struct msg_format {
    long mtype;
    char mtext[MAXFSIZE];
} msg_format;

/*
 * Validates the arguments passed to the program.
 */
void validate_args(int argc, char *argv[]) {
    if (argc != 2) {
        user_err("One argument expected (the file name)\n");
    }
    int fd = open(argv[1], O_RDONLY);
    if (fd < 0) {
        user_err("Check if the file exists and is readable\n");
    }
    if (close(fd) == -1) {
        internal_err("The file could not be closed\n");
    }
}

/*
 * Writes contents of file to the message queue.
 */
void write_to_msq(char *fname) {
    int fd = open(fname, O_RDONLY);
    if (fd < 0) {
        internal_err("The file could not be opened\n");
    }

    msg_format msg;
    msg.mtype = 1;
    if (read(fd, msg.mtext, MAXFSIZE) < 0) {
        internal_err("The program encountered an error while reading file\n");
    }

    key_t key = ftok(fname, 'a');
    int msq = msgget(key, 0666 | IPC_CREAT);

    if (msq == -1) {
        internal_err("The program could not create a message queue\n");
    }
    if (msgsnd(msq, &msg, strlen(msg.mtext), 0) == -1) {
        internal_err("The program could not send a message to the message queue\n");
    }
}

int main(int argc, char *argv[]) {
    validate_args(argc, argv);
    write_to_msq(argv[1]);
    return 0;
}
