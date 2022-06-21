import socket
import sys
from typing import List


def send_msg(socket: socket.socket, msg: str):
    socket.sendall(bytearray([0x0B, *msg.encode("UTF-8"), 0x1C, 0x0D]))


def receive_msg(socket: socket.socket) -> str:
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


if len(sys.argv) < 2:
    print("Not enough parameters: python3 unsecure-client.py <port>")
    exit(0)

hostname = "localhost"
port = int(sys.argv[1])

my_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
my_socket.connect((hostname, port))
send_msg(my_socket, "Hello!")
response = receive_msg(my_socket)
my_socket.close()

print("Answer: {}".format(response))
