import sys
import ssl
import socket


port = 1443 if len(sys.argv) < 2 else int(sys.argv[1])
message = "Welcome!"

context = ssl.SSLContext(ssl.PROTOCOL_TLSv1_2)
context.load_cert_chain("cert.pem", "key.pem", "123456")

with socket.socket(socket.AF_INET, socket.SOCK_STREAM, 0) as sock:
    sock.bind(("localhost", port))
    sock.listen()

    with context.wrap_socket(sock, server_side=True) as ssl_sock:
        conn, addr = ssl_sock.accept()
        data = conn.recv(1024)
        conn.sendall(bytearray([0x0B, *message.encode("UTF-8"), 0x1C, 0x0D]))
        conn.close()

print("From client: {}".format(data[1:-2].decode("UTF-8")))
