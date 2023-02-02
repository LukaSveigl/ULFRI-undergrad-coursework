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

/** The buffer size. */
#define BUFSIZE 1024

int main(int argc, char *argv[]) {
    int sockfd;
    struct sockaddr_in servaddr;
    char buf[BUFSIZE], sendbuf[BUFSIZE];
    int n;

    if (argc != 2) {
        user_err("Usage: client <port>\n");
    }
    if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        internal_err("socket\n");
    }

    // Set the socket parameters.
    bzero(&servaddr, sizeof(servaddr));
    servaddr.sin_family = AF_INET;
    servaddr.sin_port = htons(atoi(argv[1]));
    servaddr.sin_addr.s_addr = inet_addr("127.0.0.1");
    inet_pton(AF_INET, "127.0.0.1", &servaddr.sin_addr);

    if (connect(sockfd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0) {
        internal_err("connect\n");
    }

    printf("> ");
    while (fgets(sendbuf, BUFSIZE, stdin) != NULL) {
        if (write(sockfd, sendbuf, strlen(sendbuf)) != strlen(sendbuf)) {
            internal_err("write\n");
        }

        if (strstr(sendbuf, "quit()") != NULL) {
            printf("Quitting\n");
            close(sockfd);
            return 0;
        }

        n = read(sockfd, buf, BUFSIZE);
        buf[n] = 0;
        printf("%% ");
        puts(buf);

        bzero(buf, BUFSIZE);
        bzero(sendbuf, BUFSIZE);
        printf("> ");
    }

    return 0;
}
