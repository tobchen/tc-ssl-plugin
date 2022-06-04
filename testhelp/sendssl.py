import socket
import ssl


hostname = "localhost"
port = 6661
message = "Hello!"

context = ssl.create_default_context()

with socket.create_connection((hostname, port)) as my_socket:
    with context.wrap_socket(my_socket, server_hostname=hostname) as secure_socket:
        secure_socket.sendall(bytearray([0x0B, *message.encode("UTF-8"), 0x1C, 0x0D]))
        data = secure_socket.recv(1024)
print("Answer: {}".format(data[1:-2].decode("UTF-8")))
