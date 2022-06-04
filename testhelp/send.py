import socket


hostname = "localhost"
port = 6661
message = "Hello!"

my_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
my_socket.connect((hostname, port))
my_socket.sendall(bytearray([0x0B, *message.encode("UTF-8"), 0x1C, 0x0D]))
data = my_socket.recv(1024)
my_socket.close()

print("Answer: {}".format(data[1:-2].decode("UTF-8")))
