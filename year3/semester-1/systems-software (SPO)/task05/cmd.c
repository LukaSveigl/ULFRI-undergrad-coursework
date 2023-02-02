#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <sys/wait.h>

#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }

#define MAXLINE 512
#define MAXARGS 10

/* The command buffer. */
char buf[MAXLINE];
/* The arguments substrings. */
char *args[MAXARGS];

/* The interrupt counter. */
int interrupt_count = 0;

/*
 * Parses the given string p into substrings based on spaces, pointers to which are stored
 * in the args out parameter.
 */
void parse(char *p, char *args[]) {
    int part_count = 0;
    args[part_count++] = p;

    char *ptr = p;
    while (*ptr) {
        // Only create new substring when next character is not space.
        if (*ptr == ' ' && *(ptr + 1) != ' ') {
            *ptr = 0;
            args[part_count++] = ptr + 1;
        }
        // If consecutive spaces, skip.
        else if (*ptr == ' ' && *(ptr + 1) == ' ') {
            *ptr = 0;
        }
        ptr++;
    }
    args[part_count++] = NULL;
}

/*
 * Handles the SIGALRM signal, used to time the reset period for double SIGINT.
 */
void SIGALRM_handler(int sig) {
    interrupt_count = 0;
}

/*
 * Handles the SIGINT signal, used to force user to press CTRL+C twice to exit signal.
 */
void SIGINT_handler(int sig) {
    signal(SIGINT, SIGINT_handler);

    interrupt_count++;
    if (interrupt_count > 1) {
        printf("\n");
        kill(0, SIGTERM);
    }
    printf("\nTo exit the program press CTRL+C again in the span of 3 seconds.\n%% ");
    fflush(stdout);

    // Clear the buffer.
    memset(buf, 0, MAXLINE);
    alarm(3);
    signal(SIGALRM, SIGALRM_handler);
}

int main(int argc, char *argv[]) {
    pid_t pid;
    int status;

    signal(SIGINT, SIGINT_handler);

    printf("%% ");
    while (fgets(buf, MAXLINE, stdin) != NULL) {
        buf[strlen(buf) - 1] = 0;

        if (strcmp(buf, "quit") == 0) {
            break;
        }
        // Skip empty lines.
        if (strcmp(buf, "") == 0) {
            printf("%% ");
            continue;
        }

        // Invalid fork.
        if ((pid = fork()) < 0) {
            internal_err("Fork error\n");
        }
        // Child fork.
        else if (pid == 0) {
            parse(buf, args);
            if (execvp(args[0], args) == -1) {
                user_err("Error in command\n");
            }
        }
        // Parent fork.
        if ((pid = waitpid(pid, &status, 0)) < 0) {
            internal_err("waitpid error\n");
        }
        printf("%% ");
    }

    return 0;
}
