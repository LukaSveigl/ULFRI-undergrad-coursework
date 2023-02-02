import threading
import struct
import socket
import signal

signal.signal(signal.SIGINT, signal.SIG_DFL)

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


# funkcija za komunikacijo z odjemalcem (tece v loceni niti za vsakega odjemalca)
def client_thread(client_sock, client_addr):
    global clients

    print("[system] connected with " +
          client_addr[0] + ":" + str(client_addr[1]))
    print("[system] we now have " + str(len(clients)) + " clients")

    try:

        while True:  # neskoncna zanka
            msg_received = receive_message(client_sock)

            if not msg_received:  # ce obstaja sporocilo
                break

            print("[RKchat] [" + client_addr[0] + ":" +
                  str(client_addr[1]) + "] : " + msg_received)

            msg_parts = msg_received.split()  # razdelimo sporocilo na dele

            # ali je v sporocilu oznaka za zasebno sporocilo
            if msg_parts[2] == "#PRIVATEMSG":
                # ali uporabnik, kateremu posiljamo obstaja
                if msg_parts[3].upper() in clients:
                    # v zasebnem sporocilu zamenjamo "#PRIVATEMSG 'uporabnik'" z "PRIVATE MESSAGE: "
                    send_message(
                        clients[msg_parts[3].upper()], msg_received.replace(msg_parts[2] + " " + msg_parts[3], 'PRIVATE MESSAGE: ').upper())
                else:
                    send_message(client_sock, "This user is not logged in!")
            else:
                for client in clients.values():
                    send_message(client, msg_received.upper())
    except:
        # tule bi lahko bolj elegantno reagirali, npr. na posamezne izjeme. Trenutno kar pozremo izjemo
        pass

    # prisli smo iz neskoncne zanke
    with clients_lock:
        for uname, usock in clients.items():
            if usock == client_sock:
                clients.pop(uname)
                break

    print("[system] we now have " + str(len(clients)) + " clients")
    client_sock.close()


# kreiraj socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(("localhost", PORT))
server_socket.listen(1)

# cakaj na nove odjemalce
print("[system] listening ...")
clients = dict()
#clients = set()
clients_lock = threading.Lock()

while True:
    try:
        # pocakaj na novo povezavo - blokirajoc klic
        client_sock, client_addr = server_socket.accept()
        # sprejme prvo sporocilo -> uporabnisko ime
        first_msg = receive_message(client_sock)
        with clients_lock:
            clients[first_msg.upper()] = client_sock
            # clients.add(client_sock)

        thread = threading.Thread(
            target=client_thread, args=(client_sock, client_addr))
        thread.daemon = True
        thread.start()

    except KeyboardInterrupt:
        break

print("[system] closing server socket ...")
server_socket.close()
