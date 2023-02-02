#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }

#define MAXLINE 512
#define MAXARGS 10

void parse(char *p, char *args[]) {
    int part_count = 0;
    args[part_count++] = p;

    char *ptr = p;
    while (*ptr) {
        if (*ptr == ' ') {
            *ptr = 0;
            args[part_count++] = ptr + 1;
        }
        ptr++;
    }
    args[part_count++] = NULL;
}

int main(int argc, char *argv[]) {
    pid_t pid;
    int status;

    char buf[MAXLINE];
    char *args[MAXARGS];

    printf("%% ");
    while (fgets(buf, MAXLINE, stdin) != NULL) {
        buf[strlen(buf) - 1] = 0;

        if (strcmp(buf, "quit") == 0) {
            break;
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
