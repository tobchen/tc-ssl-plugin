# Copyright 2022 Tobias Heukäufer
#
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at https://mozilla.org/MPL/2.0/.

import sys
import socket
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
    print("Not enough parameters: python3 unsecure-server.py <port>")
    exit(0)

port = int(sys.argv[1])

with socket.socket(socket.AF_INET, socket.SOCK_STREAM, 0) as sock:
    sock.bind(("localhost", port))
    sock.listen()

    conn, addr = sock.accept()
    
    msg = receive_msg(conn)
    send_msg(conn, "You're welcome!")

    conn.close()

print("From client: {}".format(msg))
