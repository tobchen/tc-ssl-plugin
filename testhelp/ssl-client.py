import sys
import socket
import ssl
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
    print("Not enough parameters: python3 ssl-client.py <port> <use client.crt (1 or 0)>")
    exit(0)

hostname = "localhost"
port = int(sys.argv[1])

context = ssl.create_default_context()
context.check_hostname = False
context.verify_mode = ssl.CERT_NONE
if int(sys.argv[2]) == 1:
    context.load_cert_chain("client.crt", "client.key")

with socket.create_connection((hostname, port)) as my_socket:
    with context.wrap_socket(my_socket, server_hostname=hostname) as secure_socket:
        print("Wrapped socket!")

        send_msg(secure_socket, "Hello!")
        print("Sent msg!")
        
        response = receive_msg(secure_socket)
        print("Received msg!")
    
print("From server: {}".format(response))
