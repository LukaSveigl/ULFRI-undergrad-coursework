import threading
import struct
import socket
import signal

import ssl

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


def setup_SSL_context():
    # uporabi samo TLS, ne SSL
    context = ssl.SSLContext(ssl.PROTOCOL_TLSv1_2)
    # certifikat je obvezen
    context.verify_mode = ssl.CERT_REQUIRED
    # nalozi svoje certifikate
    context.load_cert_chain(certfile="server.crt",
                            keyfile="server.key")
    # nalozi certifikate CAjev, ki jim zaupas
    # (samopodp. cert. = svoja CA!)
    context.load_verify_locations('clients.pem')
    # nastavi SSL CipherSuites (nacin kriptiranja)
    context.set_ciphers('ECDHE-RSA-AES128-GCM-SHA256')
    return context


# kreiraj socket
my_ssl_ctx = setup_SSL_context()
server_socket = my_ssl_ctx.wrap_socket(
    socket.socket(socket.AF_INET, socket.SOCK_STREAM))
server_socket.bind(("localhost", PORT))
server_socket.listen(1)

# cakaj na nove odjemalce
print("[system] listening ...")
clients = dict()
clients_lock = threading.Lock()

while True:
    try:

        client_sock, client_addr = server_socket.accept()

        # Pridobimo certifikat uporabnika
        cert = client_sock.getpeercert()
        for sub in cert['subject']:
            for key, value in sub:
                # v commonName je ime uporabnika
                if key == 'commonName':
                    user = value

        with clients_lock:
            if user.upper() not in clients:  # Če uporabnik s tem certifikatom še ni prijavljen
                # Shranimo uporabnika in njegov socket
                clients[user.upper()] = client_sock
            else:  # Če je kakšen uporabnik s tem certifikatom prijavljen -> ustavi povezavo
                send_message(
                    client_sock, "User with this name already exists - closing connection")
                client_sock.close()

        print('Established SSL connection with: ', user)

        thread = threading.Thread(
            target=client_thread, args=(client_sock, client_addr))
        thread.daemon = True
        thread.start()

    except KeyboardInterrupt:
        break

print("[system] closing server socket ...")
server_socket.close()
