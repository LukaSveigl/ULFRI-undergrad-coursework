#include <stdio.h>
#include <stdlib.h>
#include <dirent.h>
#include <string.h>
#include <sys/stat.h>

#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }

/**
    Simplified version of the ls -l command.
    Displays file names, types, permissions, hard links.
*/
void lsl() {
    struct dirent *dirp;
    DIR *dp;

    if ((dp = opendir(".")) == NULL) {
        user_err("Current directory could not be opened\n");
    }

    char permissions[10];

    while ((dirp = readdir(dp)) != NULL) {
        struct stat stbuf;

        if (stat(dirp->d_name, &stbuf) == -1) {
            internal_err("Could not fetch file statistics\n");
        }

        // Get file type.
        permissions[0] = S_ISDIR(stbuf.st_mode) ? 'd' : '-';
        permissions[0] = S_ISCHR(stbuf.st_mode) ? 'c' : permissions[0];
        permissions[0] = S_ISBLK(stbuf.st_mode) ? 'b' : permissions[0];
        permissions[0] = S_ISLNK(stbuf.st_mode) ? 'l' : permissions[0];
        permissions[0] = S_ISFIFO(stbuf.st_mode) ? 'p' : permissions[0];
        permissions[0] = S_ISSOCK(stbuf.st_mode) ? 's' : permissions[0];

        // Generate string of permissions in UNIX format.
        permissions[1] = (stbuf.st_mode & S_IRUSR) ? 'r' : '-';
        permissions[2] = (stbuf.st_mode & S_IWUSR) ? 'w' : '-';
        permissions[3] = (stbuf.st_mode & S_IXUSR) ? 'x' : '-';
        permissions[4] = (stbuf.st_mode & S_IRGRP) ? 'r' : '-';
        permissions[5] = (stbuf.st_mode & S_IWGRP) ? 'w' : '-';
        permissions[6] = (stbuf.st_mode & S_IXGRP) ? 'x' : '-';
        permissions[7] = (stbuf.st_mode & S_IROTH) ? 'r' : '-';
        permissions[8] = (stbuf.st_mode & S_IWOTH) ? 'w' : '-';
        permissions[9] = (stbuf.st_mode & S_IXOTH) ? 'x' : '-';

        // Additional checks for special bits (setuid, sticky, etc.).
        if (stbuf.st_mode & S_ISUID) {
            if (permissions[3] == 'x') {
                permissions[3] = 's';
            }
            else {
                permissions[3] = 'S';
            }
        }
        if (stbuf.st_mode & S_ISGID) {
            if (permissions[6] == 'x') {
                permissions[6] = 's';
            }
            else {
                permissions[6] = 'S';
            }
        }
        if (stbuf.st_mode & S_ISVTX) {
            if (permissions[9] == 'x') {
                permissions[9] = 't';
            }
            else {
                permissions[9] = 'T';
            }
        }

        printf("%s %i %s\n", permissions, stbuf.st_nlink, dirp->d_name);
    }

    closedir(dp);
}

int main(int argc, char *argv[]) {
    lsl();

    return 0;
}
