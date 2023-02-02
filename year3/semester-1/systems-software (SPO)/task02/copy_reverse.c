#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }

#define BUFFSIZE 1024 // Read byte by byte

void validate_input(int argc, char *argv[]) {
    if (argc != 3) {
        user_err("Incorrect number of arguments, 2 expected\n");
    }

    int fd;

    fd = open(argv[1], O_RDONLY);
    if (fd == -1) {
        user_err("Check if source file exists and is readable\n");
    }
    if (close(fd) != 0) {
        internal_err("Cannot close source file\n");
    }

    fd = open(argv[2], O_WRONLY | O_CREAT, 0666);

    if (fd == -1) {
        user_err("Check if destination file exists and is writable\n");
    }
    if (close(fd) != 0) {
        internal_err("Cannot close destination file\n");
    }
}

void copy_reverse(int argc, char *argv[]) {
    int fd_src, fd_dest;

    if ((fd_src = open(argv[1], O_RDONLY)) == -1) {
        internal_err("Cannot open source file\n");
    }
    if ((fd_dest = open(argv[2], O_WRONLY)) == -1) {
        internal_err("Cannot open destination file\n");
    }

    int n, fsize;
    char buf[BUFFSIZE];

    // Set position in source file to end.
    if ((fsize = lseek(fd_src, 0, SEEK_END)) == -1) {
        internal_err("lseek error\n");
    }

    /*for (int i = fsize - 1; i >= 0; i--) {
        if (lseek(fd_src, i, SEEK_SET) == -1) {
            internal_err("lseek error\n");
        }

        if ((n = read(fd_src, buf, BUFFSIZE)) == -1) {
            internal_err("Read error\n");
        }

        printf("Buffer: %s", buf);

        if (write(fd_dest, buf, n) != n) {
            internal_err("Write error\n");
        }
    }*/
    int offset = 0;
    int new_offset = 0;

    for (int i = 1; i <= fsize; i++) {
        if (-(i * BUFFSIZE) > 0) {
            new_offset = -i * BUFFSIZE;
        }
        else {
            new_offset = -fsize;
        }

        if ((offset = lseek(fd_src, new_offset, SEEK_END)) == -1) {
            internal_err("lseek error\n");
        }

        if ((n = read(fd_src, buf, BUFFSIZE)) == -1) {
            internal_err("Read error\n");
        }

        char buf2[1];

        for (int j = strlen(buf); j >= 0; j--) {
            buf2[0] = buf[j];
            if (write(fd_dest, buf2, 1) != 1) {
                internal_err("Write error\n");
            }
        }

        if (offset == 0) {
            break;
        }
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
    copy_reverse(argc, argv);

    return 0;
}
