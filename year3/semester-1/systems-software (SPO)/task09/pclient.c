#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <strings.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/socket.h>

#include <signal.h>
#include <sys/wait.h>

#define user_err(mess) { fprintf(stderr, "Error: %s", mess); exit(1); }
#define internal_err(mess) { fprintf(stderr, "Internal error: %s", mess); exit(1); }

/** The maximum number of connections the client can open. */
#define MAXCHILDREN 10

/** The buffer size. */
#define BUFSIZE 1024

int main(int argc, char *argv[]) {
    int sockfd[MAXCHILDREN];
    struct sockaddr_in servaddr;
    char buf[BUFSIZE], sendbuf[BUFSIZE];
    int n;

    if (argc != 2) {
        user_err("Usage: client <port>\n");
    }
    for (int i = 0; i < MAXCHILDREN; i++) {
        if ((sockfd[i] = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
            internal_err("socket\n");
        }
    }

    // Set the socket parameters.
    bzero(&servaddr, sizeof(servaddr));
    servaddr.sin_family = AF_INET;
    servaddr.sin_port = htons(atoi(argv[1]));
    servaddr.sin_addr.s_addr = inet_addr("127.0.0.1");
    inet_pton(AF_INET, "127.0.0.1", &servaddr.sin_addr);

    // Create child processes, connect in each one and communicate 
    // with the server.
    int childpid;
    for (int i = 0; i < MAXCHILDREN; i++) {
        if ((childpid = fork()) == 0) {
            if (connect(sockfd[i], (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0) {
                internal_err("connect\n");
            }
            sprintf(sendbuf, "%i", getpid());
            send(sockfd[i], sendbuf, strlen(sendbuf), 0);
            int read_chars = recv(sockfd[i], buf, BUFSIZE, 0);
            buf[read_chars] = 0;
            printf("%s\n", buf);
            exit(0);
        }
    }

    // Wait for all child processes to finish.
    int wpid, status;
    while ((wpid = wait(&status)) > 0);

    return 0;
}
