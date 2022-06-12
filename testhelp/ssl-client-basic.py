from ast import arg
import sys
import socket
import ssl


hostname = "localhost"
port = 1443 if len(sys.argv) < 2 else int(sys.argv[1])
message = "Hello!"

context = ssl.create_default_context()
context.check_hostname = False
context.verify_mode = ssl.CERT_NONE

with socket.create_connection((hostname, port)) as my_socket:
    with context.wrap_socket(my_socket, server_hostname=hostname) as secure_socket:
        print("Wrapped!")
        secure_socket.sendall(bytearray([0x0B, *message.encode("UTF-8"), 0x1C, 0x0D]))
        print("Sent!")
        # TODO Read until 0x1C, 0x0D reached
        data = secure_socket.recv(1024)
        print("Read!")
    
print("From server: {}".format(data[1:-2].decode("UTF-8")))
