#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <strings.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/socket.h>

#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }
#define iswhitespace(x) ((x) == ' ' || (x) == '\n' || (x) == '\t' || (x) == '\r') ? 1 : 0

/** The receive buffer size. */
#define BUFSIZE 1024
/** The send buffer size. */
#define SENDBUFSIZE 2048
/** The listen queue size. */
#define LISTENQ 1024

/** Misc functions, */
void remove_whitespace(char *restrict str_trimmed, const char *restrict str_untrimmed);
int check_type(char *str);

/** The expression. */
struct expr_struct {
    char *expr;
} exprs;

/** Parser functions. */
char peek();
char get();
double parse_parenthesis();
double parse_mul_div();
double parse_add_sub();
double parse();

int main(int argc, char *argv[]) {
    int sockfd = 0, connfd = 0;
    struct sockaddr_in servaddr;
    char buf[BUFSIZE], sendbuf[SENDBUFSIZE];
    int n;

    if (argc != 2) {
        user_err("Usage: server <port>\n");
    }
    if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        internal_err("socket\n");
    }

    // Set the socket parameters.
    bzero(&servaddr, sizeof(servaddr));
    servaddr.sin_family = AF_INET;
    servaddr.sin_port = htons(atoi(argv[1]));
    servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    inet_pton(AF_INET, "127.0.0.1", &servaddr.sin_addr);

    int optval = -1;
    setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(optval));

    if (bind(sockfd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0) {
        internal_err("bind\n");
    }
    if (listen(sockfd, LISTENQ) < 0) {
        internal_err("listen\n");
    }

    while (1) {
        if ((connfd = accept(sockfd, NULL, NULL)) < 0) {
            internal_err("accept\n");
        }

        while ((n = read(connfd, buf, BUFSIZE)) > 0) {
            buf[n] = 0;
            printf("Received: %s", buf);
            if (strstr(buf, "quit()") != NULL) {
                printf("Client connection stopped, server quitting too\n");
                close(sockfd);
                close(connfd);
                return 0;
            }

            remove_whitespace(buf, buf);
            exprs.expr = buf;
            if (check_type(buf) == 0) {
                sprintf(sendbuf, "%i", (int)parse());
            }
            else {
                sprintf(sendbuf, "%f", parse());
            }
            if (write(connfd, sendbuf, strlen(sendbuf)) != strlen(sendbuf)) {
                internal_err("write\n");
            }
            printf("+-->Sent: %s\n", sendbuf);

            bzero(buf, BUFSIZE);
            bzero(sendbuf, SENDBUFSIZE);
        }
    }

    return 0;
}


/**
 * Removes whitespace from the string.
 */
void remove_whitespace(char *restrict str_trimmed, const char *restrict str_untrimmed) {
    while (*str_untrimmed != '\0') {
        if(!iswhitespace(*str_untrimmed)) {
            *str_trimmed = *str_untrimmed;
            str_trimmed++;
        }
        str_untrimmed++;
    }
    *str_trimmed = '\0';
}

/**
 * Checks if the string contains a double.
 */
int check_type(char *str) {
    while (*str != 0) {
        if (*str == '.') {
            return 1;
        }
        str++;
    }
    return 0;
}

/**
 * Looks at the current token without moving.
 */
char peek() {
    return *(exprs.expr);
}

/**
 * Looks at the current token while moving the pointer one token forward.
 */
char get() {
    return *(exprs.expr++);
}

/**
 * Parses the number.
 */
double number() {
    char str[20];
    int count = 0;
    while (peek() == '.' || peek() >= '0' && peek() <= '9') {
        str[count++] = get();
    }
    str[count] = 0;
    double result = atof(str);
    return result;
}

/**
 * Parses the () expression.
 */
double parse_parenthesis() {
    if (peek() == '.' || peek() >= '0' && peek() <= '9')
        return number();
    else if (peek() == '(') {
        // Move '(' token forward and discard.
        get();
        // Parse subexpression.
        double result = parse_add_sub();
        // Move ')' token forward and discard.
        get();
        return result;
    }
    else if (peek() == '-') {
        // Move '-' token forward and discard.
        get();
        return -parse_parenthesis();
    }
    // Should never happen.
    user_err("invalid expression\n");
}

/**
 * Parses the * and / operators.
 */
double parse_mul_div() {
    double result = parse_parenthesis();
    while (peek() == '*' || peek() == '/')
        if (get() == '*')
            result *= parse_parenthesis();
        else
            result /= parse_parenthesis();
    return result;
}

/**
 * Parses the + and - operators.
 */
double parse_add_sub() {
    double result = parse_mul_div();
    while (peek() == '+' || peek() == '-')
        if (get() == '+')
            result += parse_mul_div();
        else
            result -= parse_mul_div();
    return result;
}

/**
 * Starts the parsing process.
 */
double parse() {
    return parse_add_sub();
}
