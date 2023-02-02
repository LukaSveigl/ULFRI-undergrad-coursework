#include <stdio.h>
#include <fcntl.h>
#include <ctype.h>
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

/* The words in the program. Used in counting. */
char *words[MAXFSIZE];

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
 *Parses the string into words.
 */
void parse(char *p, char *args[]) {
    int part_count = 0;

    // Skip spaces at start of command.
    while (*p == ' ' || *p == '\n') {
        p++;
    }

    args[part_count++] = p;

    // Trim spaces from end of command.
    int end = strlen(p) - 1;
    while (isspace((unsigned char) p[end])) {
        end--;
    }
    p[end + 1] = 0;

    char *ptr = p;
    while (*ptr) {
        // Only create new substring when next character is not space.
        if ((*ptr == ' ' || *ptr == '\n') && (*(ptr + 1) != ' ' && *(ptr + 1) != '\n')) {
            *ptr = 0;
            args[part_count++] = ptr + 1;
        }
        // If consecutive spaces, skip.
        else if ((*ptr == ' ' || *ptr == '\n') && (*(ptr + 1) == ' ' || *(ptr + 1) == '\n')) {
            *ptr = 0;
        }
        ptr++;
    }
    args[part_count++] = NULL;
}

/*
 * Reads message from message queue and counts words.
 */
void read_from_msq(char *fname) {
    int fd = open(fname, O_RDONLY);
    if (fd < 0) {
        internal_err("The file could not be opened\n");
    }

    msg_format msg;

    key_t key = ftok(fname, 'a');
    int msq = msgget(key, 0644);

    msg.mtype = 1;

    if (msq == -1) {
        internal_err("The program could not open a message queue\n");
    }
    if (msgrcv(msq, &msg, sizeof(msg.mtext), 1, 0) == -1) {
        internal_err("The program could not receive a message from the message queue\n");
    }

    parse(msg.mtext, words);

    int count = 0;
    while (words[count] != 0) {
        count++;
    }

    printf("Number of words in file: %i\n", count);

    msgctl(msq, IPC_RMID, NULL);
}

int main(int argc, char *argv[]) {
    validate_args(argc, argv);
    read_from_msq(argv[1]);
    return 0;
}
