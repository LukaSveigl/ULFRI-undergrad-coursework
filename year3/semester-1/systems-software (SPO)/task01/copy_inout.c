#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

#define BUFFSIZE 8192
#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }

void print_files(int argc, char *argv[]) {
    int fd, n;
    char buf[BUFFSIZE];

    for (int i = 1; i < argc; i++) {
        fd = open(argv[i], O_RDONLY);

        if (fd == -1) {
            user_err("Check if file exists and is readable\n");
        }

        while ((n = read(fd, buf, BUFFSIZE)) > 0) {
            if (write(STDOUT_FILENO, buf, n) != n) {
                internal_err("Write error\n");
            }
        }
        if (n < 0) {
            internal_err("Read error\n");
        }

        if (close(fd) != 0) {
            internal_err("Cannot close source file\n");
        }
    }
}

void copy_inout() {
    int n;
    char buf[BUFFSIZE];

    while ((n = read(STDIN_FILENO, buf, BUFFSIZE)) > 0) {
        if (write(STDOUT_FILENO, buf, n) != n) {
            internal_err("Write error\n");
        }
    }
    if (n < 0) {
        internal_err("Read error\n");
    }
}

int main(int argc, char *argv[]) {
    if (argc == 1) {
        copy_inout();
    }
    else {
        print_files(argc, argv);
    }

    return 0;
}