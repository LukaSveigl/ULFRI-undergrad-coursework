from datetime import datetime

import socket
import struct
import sys
import threading

import ssl

# Uporaba za preverjanje datotek
import glob

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


def setup_SSL_context(name):
    # uporabi samo TLS, ne SSL
    context = ssl.SSLContext(ssl.PROTOCOL_TLSv1_2)
    # certifikat je obvezen
    context.verify_mode = ssl.CERT_REQUIRED
    # nalozi svoje certifikate
    context.load_cert_chain(certfile=name + ".crt",
                            keyfile=name + ".key")
    # nalozi certifikate CAjev (samopodp. cert.= svoja CA!)
    context.load_verify_locations('server.crt')
    # nastavi SSL CipherSuites (nacin kriptiranja)
    context.set_ciphers('ECDHE-RSA-AES128-GCM-SHA256')
    return context


def check_username(name):  # Funkcija, ki pogleda če tak certifikat obstaja
    if glob.glob(name + ".crt"):
        return True
    return False


# Vprasaj uporabnika za njegovo ime
name = ""
while True:
    name = input("Enter your name: ")
    # Če je ime vpisano in certifikat obstaja
    if name and check_username(name):
        break

# povezi se na streznik
print("[system] connecting to chat server ...")

my_ssl_ctx = setup_SSL_context(name.lower())
sock = my_ssl_ctx.wrap_socket(
    socket.socket(socket.AF_INET, socket.SOCK_STREAM))
sock.connect(("localhost", PORT))


print("[system] connected!")
print("[system] to send a private message type #PRIVATEMSG username message")

# zazeni message_receiver funkcijo v loceni niti
thread = threading.Thread(target=message_receiver)
thread.daemon = True
thread.start()

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
