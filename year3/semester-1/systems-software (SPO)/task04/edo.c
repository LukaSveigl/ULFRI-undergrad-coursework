#include <stdio.h>
#include <fcntl.h>
#include <ctype.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>

#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }

#define FILE_INDEX 1
#define COMMAND_INDEX 2
#define OPTIONAL_INDEX 3
#define NO_LINE_PARAM -1

#define APPEND_BUFFSIZE 1024

void validate_input(int argc, char *argv[]);
void execute_command(char *filename, char *command, int optional_line_parameter);
void e_append(char *filename);
void e_delete(char *filename, int line);
void e_insert(char *filename, int line);

/** Utility functions and datatypes. */
typedef struct {
    int first_newline_offset;
    int second_newline_offset;
    int current_offset;
    int newline_counter;
} offset_struct;

int get_number_of_lines(int fd);
offset_struct calculate_offsets(int fd, int line, int fsize);

int str_ends_with(const char *s, const char *suffix) {
    size_t slen = strlen(s);
    size_t suffix_len = strlen(suffix);

    return suffix_len <= slen && !strcmp(s + slen - suffix_len, suffix);
}

int main(int argc, char *argv[]) {
    validate_input(argc, argv);
    if (argc == 3) {
        execute_command(argv[FILE_INDEX], argv[COMMAND_INDEX], NO_LINE_PARAM);
    }
    else {
        execute_command(argv[FILE_INDEX], argv[COMMAND_INDEX], atoi(argv[OPTIONAL_INDEX]));
    }

    return 0;
}

/**
 * Validates the program arguments.
 */
void validate_input(int argc, char *argv[]) {
    if (argc != 3 && argc != 4) {
        user_err("Incorrect number of arguments, 2 or 3 expected\n");
    }

    // Validate file. If inserting or deleting, do not create new file.
    int fd;
    if (strcmp(argv[COMMAND_INDEX], "a") == 0) {
        fd = open(argv[FILE_INDEX], O_RDWR | O_CREAT | O_APPEND, 0640);
    }
    else {
        fd = open(argv[FILE_INDEX], O_RDWR | O_APPEND);
    }
    if (fd == -1) {
        user_err("Check if file exists and is readable and writable\n");
    }
    if (close(fd) != 0) {
        internal_err("Cannot close file\n");
    }

    // Validate second argument
    if (strcmp(argv[COMMAND_INDEX], "a") != 0 &&
        strcmp(argv[COMMAND_INDEX], "d") != 0 &&
        strcmp(argv[COMMAND_INDEX], "i") != 0) {
        user_err("Second argument (specfiying mode), must be one of a, d, i\n");
    }

    // Validate deletion or insertion arguments.
    if (strcmp(argv[COMMAND_INDEX], "d") == 0 || strcmp(argv[COMMAND_INDEX], "i") == 0) {
        if (argc != 4) {
            user_err("If inserting or deleting, specify the line number\n");
        }
        if (atoi(argv[OPTIONAL_INDEX]) < 1) {
            user_err("If inserting or deleting, third argument must be positive number\n");
        }

    }
}

/**
 * Executes the command on the given file.
 */
void execute_command(char *filename, char *command, int optional_line_parameter) {
    if (strcmp(command, "a") == 0) {
        e_append(filename);
    }
    else if (strcmp(command, "d") == 0) {
        e_delete(filename, optional_line_parameter);
    }
    else if (strcmp(command, "i") == 0) {
        e_insert(filename, optional_line_parameter);
    }
    else {
        internal_err("The program has encountered an unexpected error, shutting down\n");
    }
}

/**
 * Reads the standard input stream and appends it to the file.
 */
void e_append(char *filename) {
    int fd = open(filename, O_RDWR | O_APPEND);
    if (fd == -1) {
        internal_err("Could not open file\n");
    }

    int fsize;
    if ((fsize = lseek(fd, 0, SEEK_END)) == -1) {
        internal_err("lseek error\n");
    }

    // If file not empty, set file descriptor to end.
    if (fsize > 0) {
        if (lseek(fd, -1, SEEK_END) == -1) {
            internal_err("lseek error\n");
        }
    }
    // If file empty, set file descriptor to start.
    else {
        if (lseek(fd, 0, SEEK_SET) == -1) {
            internal_err("lseek error\n");
        }
    }

    int n;
    char buf[APPEND_BUFFSIZE];

    // Write from STDIN to file.
    while ((n = read(STDIN_FILENO, buf, APPEND_BUFFSIZE)) > 0) {
        if (strstr(buf, "quit") == NULL) {
            if (write(fd, buf, n) != n) {
                internal_err("Write error\n");
            }
        }
        else {
            char *p = buf;
            char *pos = strstr(buf, "\nquit\n");
            int index = pos - p;
            write(fd, buf, index);
            break;
        }
    }
    if (n < 0) {
        internal_err("Read error\n");
    }

    if (close(fd) != 0) {
        internal_err("Cannot close source file\n");
    }
}

/**
 * Deletes the given line. If the line number is over the max line number,
 * nothing happens.
 */
void e_delete(char *filename, int line) {
    int fd = open(filename, O_RDWR | O_APPEND);
    if (fd == -1) {
        internal_err("Could not open file\n");
    }

    int lines = get_number_of_lines(fd);

    if (line > lines) {
        return;
    }

    int fsize;
    if ((fsize = lseek(fd, 0, SEEK_END)) == -1) {
        internal_err("lseek error\n");
    }

    int bytes_read;
    char buf[1];

    offset_struct os = calculate_offsets(fd, line, fsize);

    if (lseek(fd, 0, SEEK_SET) == -1) {
        internal_err("lseek error\n");
    }

    // Read entire file into buffer.
    int const buffer_size = fsize;
    char *move_buffer = malloc(buffer_size);
    bytes_read = read(fd, move_buffer, buffer_size);
    if (bytes_read < 0) {
        internal_err("Read error\n");
    }

    if (close(fd) != 0) {
        internal_err("Cannot close source file\n");
    }

    // Clear file.
    fd = open(filename, O_RDWR | O_TRUNC);
    if (fd == -1) {
        internal_err("Could not open file\n");
    }

    char tmp_buf[1];
    // Write entire buffer into file, but skip the bytes between the start and end of row.
    for (int i = 0; i < buffer_size; i++) {
        if (i < os.first_newline_offset || i >= os.second_newline_offset) {
            tmp_buf[0] = move_buffer[i];
            if (write(fd, tmp_buf, 1) != 1) {
                internal_err("Write error\n");
            }
        }
    }

    if (close(fd) != 0) {
        internal_err("Cannot close source file\n");
    }
}

/**
 * Inserts text at the given line. If the line number is over the max line number,
 * it inserts at the end of the file.
 */
void e_insert(char *filename, int line) {
    int fd = open(filename, O_RDWR | O_APPEND);
    if (fd == -1) {
        internal_err("Could not open file\n");
    }

    int lines = get_number_of_lines(fd);

    int fsize;
    if ((fsize = lseek(fd, 0, SEEK_END)) == -1) {
        internal_err("lseek error\n");
    }

    int bytes_read;
    char buf[1];

    offset_struct os = calculate_offsets(fd, line, fsize);

    if (line > lines) {
        user_err("Given line does not exist in file\n");
    }

    if (lseek(fd, 0, SEEK_SET) == -1) {
        internal_err("lseek error\n");
    }

    // Read entire file into buffer.
    int const buffer_size = fsize;
    char *move_buffer = malloc(buffer_size);
    bytes_read = read(fd, move_buffer, buffer_size);
    if (bytes_read < 0) {
        internal_err("Read error\n");
    }

    if (close(fd) != 0) {
        internal_err("Cannot close source file\n");
    }

    fd = open(filename, O_RDWR | O_TRUNC);
    if (fd == -1) {
        internal_err("Could not open file\n");
    }

    char tmp_buf[1];

    char cmd_buf[4];
    int cmd_count = 0;

    int offset;
    if (line == 1) {
        offset = os.first_newline_offset;
    }
    else {
        offset = os.first_newline_offset + 1;
    }

    // Write contents of buffer into file, until the line to insert is reached. There, get input
    // from STDIN and write to file, until .q occurs on a new line. After that, write the rest of the
    // buffer into file.
    for (int i = 0; i < buffer_size; i++) {
        if (i == offset) {
            while ((bytes_read = read(STDIN_FILENO, tmp_buf, 1)) > 0) {

                if (write(fd, tmp_buf, 1) != 1) {
                    internal_err("Write error\n");
                }
                if (tmp_buf[0] == '\n') {
                    break;
                }
            }
        }
        tmp_buf[0] = move_buffer[i];
        if (write(fd, tmp_buf, 1) != 1) {
            internal_err("Write error\n");
        }
    }

    if (close(fd) != 0) {
        internal_err("Cannot close file\n");
    }
}

/** Utility functions. */

/**
 * Gets the number of lines in given file.
 */
int get_number_of_lines(int fd) {
    int bytes_read, fsize;
    char buf[1];

    if ((fsize = lseek(fd, 0, SEEK_END)) == -1) {
        internal_err("lseek error\n");
    }

    int ncount = 0;
    int offset = 1;

    // Get offset of n-th newline from end.
    for (int i = 0; i < fsize; i++) {
        if (lseek(fd, i, SEEK_SET) == -1) {
            internal_err("lseek error\n");
        }

        if ((bytes_read = read(fd, buf, 1)) == -1) {
            internal_err("Read error\n");
        }

        if (buf[0] == '\n') {
            ncount++;
        }
    }

    return ncount;
}

/**
 * Calculates where the given line begins and ends.
 */
offset_struct calculate_offsets(int fd, int line, int fsize) {
    offset_struct os;
    os.newline_counter = 1;
    os.first_newline_offset = -1;
    os.second_newline_offset = -1;
    os.current_offset = -1;

    int bytes_read;
    char buf[1];

    for (int i = 0; i < fsize; i++) {
        if (lseek(fd, i, SEEK_SET) == -1) {
            internal_err("lseek error\n");
        }

        if ((bytes_read = read(fd, buf, 1)) == -1) {
            internal_err("Read error\n");
        }

        os.current_offset++;
        if (buf[0] == '\n') {
            os.newline_counter++;
        }

        if (os.newline_counter == line && os.first_newline_offset == -1) {
            os.first_newline_offset = os.current_offset;
        }
        else if (os.newline_counter == line + 1) {
            os.second_newline_offset = os.current_offset;
            if (line == 1) {
                os.second_newline_offset++;
            }
            break;
        }
    }

    return os;
}
