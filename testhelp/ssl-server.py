import sys
import ssl
import socket
from ssl import SSLSocket
from typing import List


def send_msg(socket: SSLSocket, msg: str):
    socket.sendall(bytearray([0x0B, *msg.encode("UTF-8"), 0x1C, 0x0D]))


def receive_msg(socket: SSLSocket) -> str:
    received: List[int] = list()

    read_0x1c = False
    read_0x0d = False
    while not (read_0x1c and read_0x0d):
        data = socket.recv(1024)

        for b in data:
            received.append(b)
            if b == 0x1C:
                read_0x1c = True
            elif read_0x1c and b == 0x0D:
                read_0x0d = True
            else:
                read_0x1c = False
    
    return bytes(received[1:-2]).decode("UTF-8")


if len(sys.argv) < 3:
    print("Not enough parameters: python3 ssl-server.py <port> <do client auth (1 or 0)>")
    exit(0)

port = int(sys.argv[1])
message = "Welcome!"

context = ssl.SSLContext(ssl.PROTOCOL_TLS)
context.load_cert_chain("server.crt", "server.key")

with socket.socket(socket.AF_INET, socket.SOCK_STREAM, 0) as sock:
    sock.bind(("localhost", port))
    sock.listen()

    with context.wrap_socket(sock, server_side=True) as ssl_sock:
        conn, addr = ssl_sock.accept()
        
        msg = receive_msg(conn)
        send_msg(conn, "You're welcome!")

        conn.close()

print("From client: {}".format(msg))
