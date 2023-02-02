#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }

#define BUFFSIZE 1 // Read byte by byte.
#define DEFAULTN 5

int len(char *string) {
    int length = strlen(string) - 2; // Don't include -n prefix of flag.

    char *buf = malloc(length);
    strncpy(buf, string + 2, strlen(string));

    return atoi(buf);
}

void validate_input(int argc, char *argv[]) {
    if (argc != 2 && argc != 3) {
        user_err("Incorrect number of arguments, 1 or 2 expected\n");
    }

    char *fname = argv[1];

    if (argc == 3) {
        if (strlen(argv[1]) < 3 ) {
            user_err("Incorrect first argument, expected format -n<value> or -m<value>\n");
        }
        if (argv[1][0] != '-' || argv[1][1] != 'n' && argv[1][1] != 'm') {
            user_err("Incorrect first argument, expected format -n<value> or -m<value>\n");
        }

        fname = argv[2];
    }

    int fd;

    fd = open(fname, O_RDONLY);
    if (fd == -1) {
        user_err("Check if file exists and is readable\n");
    }
    if (close(fd) != 0) {
        internal_err("Cannot close file\n");
    }
}

void rep(int argc, char *argv[]) {
    int n = DEFAULTN;
    char *fname = argv[1];

    if (argc == 3) {
        n = len(argv[1]);
        fname = argv[2];

        if (n == 0) {
            return;
        }
    }

    int fd, k, fsize;
    char buf[BUFFSIZE];

    if ((fd = open(fname, O_RDONLY)) == -1) {
        internal_err("Cannot open file\n");
    }

    if ((fsize = lseek(fd, 0, SEEK_END)) == -1) {
        internal_err("lseek error\n");
    }

    int ncount = 0;
    int offset = 1;

    // Get offset of n-th newline from end.
    for (int i = fsize - 1; i >= 0; i--) {
        if (lseek(fd, i, SEEK_SET) == -1) {
            internal_err("lseek error\n");
        }

        if ((k = read(fd, buf, BUFFSIZE)) == -1) {
            internal_err("Read error\n");
        }

        offset++;
        if (buf[0] == '\n') {
            ncount++;
        }
        if (ncount >= n) {
            break;
        }
    }

    ncount = 0;

    // Print required no. of lines by printing byte by byte.
    for (int i = fsize - offset; i < fsize; i++) {
        if (lseek(fd, i, SEEK_SET) == -1) {
            internal_err("lseek error\n");
        }

        if ((k = read(fd, buf, BUFFSIZE)) == -1) {
            internal_err("Read error\n");
        }

        if (buf[0] == '\n') {
            ncount++;
        }

        if (argc == 3) {
            // If mode "n" print every line.
            if (argv[1][1] == 'n') {
                if (write(STDOUT_FILENO, buf, k) != k) {
                    internal_err("Write error\n");
                }
            }
            // If mode "m" print every second line.
            else {
                if (ncount % 2 == 0) {
                    if (write(STDOUT_FILENO, buf, k) != k) {
                        internal_err("Write error\n");
                    }
                }
            }
        }
        else {
            if (write(STDOUT_FILENO, buf, k) != k) {
                internal_err("Write error\n");
            }
        }
    }

    if (close(fd) != 0) {
        internal_err("Cannot close file\n");
    }
}

int main(int argc, char *argv[]) {
    validate_input(argc, argv);
    rep(argc, argv);

    return 0;
}
