#include <stdio.h>
#include <fcntl.h>
#include <ctype.h>
#include <unistd.h>
#include <stdlib.h>
#include <dirent.h>
#include <string.h>
#include <sys/stat.h>

#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }

/**
    Validates if the arguments passed to the program are valid.
    First argument must be the mode, given either as a sequence of
    4 digits between 0 and 7 or as one of: u+s, u-s, g+s, g-s.
    Second argument must be a valid file name.
*/
void validate_input(int argc, char *argv[]) {
    if (argc != 3) {
        user_err("Incorrect number of arguments, 2 expected\n");
    }

    // Check if mode is valid (either 4 digits or 3 characters).
    if (strlen(argv[1]) == 3) {
        if (argv[1][0] != 'u' && argv[1][0] != 'g') {
            user_err("First character must be either 'u' or 'g'\n");
        }
        if (argv[1][1] != '+' && argv[1][1] != '-') {
            user_err("Second character must be either '+' or '-'\n");
        }
        if (argv[1][2] != 's') {
            user_err("Third character must be 's'\n");
        }
    }
    else if (strlen(argv[1]) == 4) {
        for (int i = 0; i < strlen(argv[1]); i++) {
            if (!isdigit(argv[1][i])) {
                user_err("Wrong mode format! Expected 4 digits or 3 character string\n");
            }
            if (argv[1][i] - '0' < 0 || argv[1][i] - '0' > 7) {
                user_err("Digit must be between 0 and 7 (inclusive)\n");
            }
        }
    }
    else {
        user_err("Wrong mode format! Expected 4 digits or 3 character string\n");
    }


    struct stat stbuf;
    if (stat(argv[2], &stbuf) == -1) {
        internal_err("Check if file exists\n");
    }
}

/**
    Calculates the permissions of the given file.
*/
void get_permissions(const char *filename, char *permissions) {
    struct stat stbuf;
    if (stat(filename, &stbuf) == -1) {
        internal_err("Could not fetch file statistics\n");
    }

    // Generate string of permissions in UNIX format.
    permissions[0] = (stbuf.st_mode & S_IRUSR) ? 'r' : '-';
    permissions[1] = (stbuf.st_mode & S_IWUSR) ? 'w' : '-';
    permissions[2] = (stbuf.st_mode & S_IXUSR) ? 'x' : '-';
    permissions[3] = (stbuf.st_mode & S_IRGRP) ? 'r' : '-';
    permissions[4] = (stbuf.st_mode & S_IWGRP) ? 'w' : '-';
    permissions[5] = (stbuf.st_mode & S_IXGRP) ? 'x' : '-';
    permissions[6] = (stbuf.st_mode & S_IROTH) ? 'r' : '-';
    permissions[7] = (stbuf.st_mode & S_IWOTH) ? 'w' : '-';
    permissions[8] = (stbuf.st_mode & S_IXOTH) ? 'x' : '-';

    // Additional checks for special bits (setuid, sticky, etc.).
    if (stbuf.st_mode & S_ISUID) {
        if (permissions[2] == 'x') {
            permissions[2] = 's';
        }
        else {
            permissions[2] = 'S';
        }
    }
    if (stbuf.st_mode & S_ISGID) {
        if (permissions[5] == 'x') {
            permissions[5] = 's';
        }
        else {
            permissions[5] = 'S';
        }
    }
    if (stbuf.st_mode & S_ISVTX) {
        if (permissions[8] == 'x') {
            permissions[8] = 't';
        }
        else {
            permissions[8] = 'T';
        }
    }
}

/**
    Changes the permissions of the given file according
    to the mode.
*/
void my_chmod(const char *filename, char *mode) {
    struct stat stbuf;

    if (stat(filename, &stbuf) == -1) {
        internal_err("Could not fetch file statistics\n");
    }

    mode_t bmode = stbuf.st_mode;

    // If mode is given as 3 character string, add or remove the specific user permission.
    if (strlen(mode) == 3) {
        if (mode[0] == 'u') {
            if (mode[1] == '+') {
                bmode |= S_ISUID;
            }
            else {
                bmode ^= S_ISUID;
            }
        }
        else {
            if (mode[1] == '+') {
                bmode |= S_ISGID;
            }
            else {
                bmode ^= S_ISGID;
            }
        }
    }
    // If mode is given as 4 integer sequence, use that as new mode.
    else {
        char *ep;
        bmode = strtol(mode, &ep, 8);
    }

    if (chmod(filename, bmode) == -1) {
        internal_err("Could not change file permissions\n");
    }

    char permissions[9];
    get_permissions(filename, permissions);
    printf("New permissions: %s\n", permissions);
}

int main(int argc, char *argv[]) {
    validate_input(argc, argv);
    my_chmod(argv[2], argv[1]);

    return 0;
}
