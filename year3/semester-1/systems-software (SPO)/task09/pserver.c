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

/** The maximum number of connections the server can accept. */
#define MAXCONNECTIONS 10

/** The receive buffer size. */
#define BUFSIZE 1024
/** The send buffer size. */
#define SENDBUFSIZE 2048
/** The listen queue size. */
#define LISTENQ 1024

/**
 * The SIGCHILD handler, used to prevent zombie processes. 
 */
void child_handler() {
    pid_t pid;
    int status;

    // Wait for child to quit, this is to prevent zombies.
    while((pid = waitpid(-1, &status, WNOHANG)) > 0);
}

int main(int argc, char *argv[]) {
    pid_t children[MAXCONNECTIONS];
    int clients = 0;


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

    // Register the SIGCHILD handler.
    signal(SIGCHLD, child_handler);

    // Accept connections from client, for each connection create child process,
    // assemble message and send.
    int childpid;
    while (1) {
        if ((connfd = accept(sockfd, NULL, NULL)) < 0) {
            internal_err("accept\n");
        }

        if ((childpid = fork()) == 0) {            
            recv(connfd, buf, BUFSIZE, 0);
            int client_child_pid = atoi(buf);
            printf("Received connection from client child %i\n", client_child_pid);
            sprintf(sendbuf, "Server child (%i) serving client child (%i)", getpid(), client_child_pid);
            send(connfd, sendbuf, strlen(sendbuf), 0);
            bzero(sendbuf, SENDBUFSIZE);
            exit(0);
        }
    }

    return 0;
}
