#include <stdio.h>
#include <ctype.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <sys/wait.h>

#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }

#define MAXLINE 512
#define MAXCOMS 2
#define MAXARGS 10

#define SRCINDEX 0
#define DSTINDEX 1

/* The command buffer. */
char buf[MAXLINE];
/* The commands. */
char *coms[MAXCOMS];
/* The arguments substrings. */
char *args[MAXCOMS][MAXARGS];

/* The interrupt counter. */
int interrupt_count = 0;

/*
 * Parses the given string p into command substring based on delimiter '-->', pointers to which
 * are stored in the args out parameter.
 */
void parse_commands(char *p, char *args[]) {
    int part_count = 0;

    // Skip spaces at start of string.
    while (*p == ' ') {
        p++;
    }

    args[part_count++] = p;
    // If pipe not found, no need to parse.
    char *ptr = strstr(p, "-->");
    if (ptr == NULL) {
        args[part_count++] = 0;
        return;
    }
    *(ptr - 1) = 0;
    args[part_count++] = ptr + 3;
}

/*
 * Parses the given command string p into argument substrings based on spaces,
 * pointers to which are stored in the args out parameter.
 */
void parse_arguments(char *p, char *args[]) {
    int part_count = 0;

    // Skip spaces at start of command.
    while (*p == ' ') {
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

/*
 * Code to execute in source child process fork. Closes pipe and duplicates write end into stdout,
 * and executes the given command.
 */
void exec_src_child(int *fd) {
    if (coms[DSTINDEX] != 0) {
        dup2(fd[1], STDOUT_FILENO);
        close(fd[0]);
        close(fd[1]);
    }
    parse_arguments(coms[SRCINDEX], args[SRCINDEX]);
    if (execvp(args[SRCINDEX][0], args[SRCINDEX]) == -1) {
        user_err("Error in command\n");
    }
}

/*
 * Code to execute in destination child process fork. Closes pipe and duplicates write end into
 * stdout, and executes the given command.
 */
void exec_dst_child(int *fd) {
    dup2(fd[0], STDIN_FILENO);
    close(fd[0]);
    close(fd[1]);
    parse_arguments(coms[DSTINDEX], args[DSTINDEX]);
    if (execvp(args[DSTINDEX][0], args[DSTINDEX]) != -1) {
        user_err("Error in command\n");
    }
}

/*
 * Code to execute in parent. Closes pipe to avoid duplication, and waits for child processes to
 * end.
 */
void exec_parent(int *fd, int pid_src, int pid_dst) {
    int status_src, status_dst;
    if (coms[DSTINDEX] != 0) {
        // If 2 children, close pipe in parent to avoid duplication.
        close(fd[0]);
        close(fd[1]);
    }
    if (waitpid(pid_src, &status_src, 0) < 0) {
        internal_err("src waitpid error\n");
    }
    if (coms[DSTINDEX] != 0) {
        if (waitpid(pid_dst, &status_dst, 0) < 0) {
            internal_err("dst waitpid error\n");
        }
    }
}

int main(int argc, char *argv[]) {
    pid_t pid_src = -1, pid_dst = -1;
    int fd[2];

    if (signal(SIGINT, SIGINT_handler) == SIG_ERR) {
        internal_err("signal error\n");
    }

    // 1. Parse line into commands.
    // 2. Parse commands into arguments
    // 3. Execute
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

        parse_commands(buf, coms);

        if (pipe(fd) == -1) {
            internal_err("pipe error\n");
        }

        if ((pid_src = fork()) < 0) {
            internal_err("Error creating src child process\n");
        }
        if (pid_src == 0) {
            // Src child code.
            exec_src_child(fd);
        }
        else {
            // If 2 commands specified, create destination child.
            if (coms[1] != 0) {
                if ((pid_dst = fork()) < 0) {
                    internal_err("Error creating dst child process\n");
                }
            }
            if (pid_dst == 0) {
                // Destination child code.
                exec_dst_child(fd);
            }
            if (pid_dst != 0 && pid_src != 0) {
                // Parent code.
                exec_parent(fd, pid_src, pid_dst);
            }
        }

        printf("%% ");
    }

    return 0;
}
