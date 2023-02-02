#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

#define BUFFSIZE 512
#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }

void validate_input(int argc, char *argv[]) {
    // Validate number of arguments.
    if (argc != 3) {
        user_err("Incorrect number of arguments, 2 expected\n");
    }

    int fd, val, accmode;

    // Is source file readable?
    fd = open(argv[1], O_RDONLY | O_APPEND);

    val = fcntl(fd, F_GETFL, 0);
    accmode = val & O_ACCMODE;

    if (accmode != O_RDONLY && accmode != O_RDWR) {
        user_err("Check if source file exists and is readable\n");
    }
    if (close(fd) != 0) {
        internal_err("Cannot close source file\n");
    }

    // Is destination file writable?
    // Note: mode 0664 means permissions rw-rw-r-- for
    // creating the file.
    fd = open(argv[2], O_WRONLY | O_CREAT, 0664);

    val = fcntl(fd, F_GETFL, 0);
    accmode = val & O_ACCMODE;

    if (accmode != O_WRONLY && accmode != O_RDWR) {
        user_err("Check if destination file is writable\n");
    }
    if (close(fd) != 0) {
        internal_err("Cannot close destination file\n");
    }
}

int copy_file(char *argv[]) {
    int fd_src, fd_dest;

    if ((fd_src = open(argv[1], O_RDONLY)) == -1) {
        internal_err("Cannot open source file\n");
    }
    if((fd_dest = open(argv[2], O_WRONLY | O_TRUNC)) == -1) {
        internal_err("Cannot open destination file\n");
    }


    int n;
    char buf[BUFFSIZE];

    while ((n = read(fd_src, buf, BUFFSIZE)) > 0) {
        if (write(fd_dest, buf, n) != n) {
            internal_err("Write error\n");
        }
    }
    if (n < 0) {
        internal_err("Read error\n");
    }


    if (close(fd_src) != 0) {
        internal_err("Cannot close source file\n");
    }
    if (close(fd_dest) != 0) {
        internal_err("Cannot close destination file\n");
    }
}

int main(int argc, char *argv[]) {
    validate_input(argc, argv);
    copy_file(argv);

    return 0;
}