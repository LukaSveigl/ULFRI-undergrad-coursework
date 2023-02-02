from datetime import datetime

import socket
import struct
import sys
import threading

PORT = 1234
HEADER_LENGTH = 2


def receive_fixed_length_msg(sock, msglen):
    message = b''
    while len(message) < msglen:
        chunk = sock.recv(msglen - len(message))  # preberi nekaj bajtov
        if chunk == b'':
            raise RuntimeError("socket connection broken")
        message = message + chunk  # pripni prebrane bajte sporocilu

    return message


def receive_message(sock):
    header = receive_fixed_length_msg(sock,
                                      HEADER_LENGTH)  # preberi glavo sporocila (v prvih 2 bytih je dolzina sporocila)
    # pretvori dolzino sporocila v int
    message_length = struct.unpack("!H", header)[0]

    message = None
    if message_length > 0:  # ce je vse OK
        message = receive_fixed_length_msg(
            sock, message_length)  # preberi sporocilo
        message = message.decode("utf-8")

    return message


def send_message(sock, message):
    # pretvori sporocilo v niz bajtov, uporabi UTF-8 kodno tabelo
    encoded_message = message.encode("utf-8")

    # ustvari glavo v prvih 2 bytih je dolzina sporocila (HEADER_LENGTH)
    # metoda pack "!H" : !=network byte order, H=unsigned short
    header = struct.pack("!H", len(encoded_message))

    # najprj posljemo dolzino sporocilo, slee nato sporocilo samo
    message = header + encoded_message
    sock.sendall(message)


# message_receiver funkcija tece v loceni niti
def message_receiver():
    while True:
        msg_received = receive_message(sock)
        if len(msg_received) > 0:  # ce obstaja sporocilo
            print("[RKchat] " + msg_received)  # izpisi


# uprasaj uporabnika za njegovo ime
name = ""
while not name:
    name = input("Enter your name: ")

# povezi se na streznik
print("[system] connecting to chat server ...")
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(("localhost", PORT))
print("[system] connected!")
print("[system] to send a private message type #PRIVATEMSG username message")

# zazeni message_receiver funkcijo v loceni niti
thread = threading.Thread(target=message_receiver)
thread.daemon = True
thread.start()

# poslji prvo sporocilo strezniku - streznik si zapomni uporabnisko ime
send_message(sock, name)

# pocakaj da uporabnik nekaj natipka in poslji na streznik
while True:
    try:

        msg_send = ""
        while not msg_send:
            msg_send = input("")

        # zlepimo sporocilo v format "posiljatelj", "cas": "sporocilo"
        msg_send = name + ", " + datetime.now().strftime("%H:%M:%S") + \
            ": " + msg_send

        send_message(sock, msg_send)
    except KeyboardInterrupt:
        sys.exit()
