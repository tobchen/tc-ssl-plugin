package de.tobchen.tobyconnects.tcssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

import com.mirth.connect.connectors.tcp.DefaultTcpConfiguration;
import com.mirth.connect.connectors.tcp.TcpReceiver;
import com.mirth.connect.donkey.model.channel.ConnectorPluginProperties;
import com.mirth.connect.donkey.server.channel.Connector;

public class TCSSLConfiguration extends DefaultTcpConfiguration {

    private ConfigType type = ConfigType.DEFAULT;

    private SSLSocketFactory socketFactory;
    private SSLServerSocketFactory serverSocketFactory;

    @Override
    public void configureConnectorDeploy(Connector connector) throws Exception {
        super.configureConnectorDeploy(connector);

        type = ConfigType.DEFAULT;

        if (connector instanceof TcpReceiver) {
            for (ConnectorPluginProperties properties : connector.getConnectorProperties().getPluginProperties()) {
                if (properties instanceof TCSSLPluginProperties) {
                    TCSSLPluginProperties sslProperties = (TCSSLPluginProperties) properties;
                    
                    if (sslProperties.isEnabled()) {
                        type = ConfigType.SSL_RECEIVER;

                        SSLContext context = SSLContext.getDefault();
                        socketFactory = context.getSocketFactory();
                        serverSocketFactory = context.getServerSocketFactory();
                    }

                    break;
                }
            }
        }
    }

    @Override
    public ServerSocket createServerSocket(int port, int backlog) throws IOException {
        switch (type) {
        case SSL_RECEIVER:
            return serverSocketFactory.createServerSocket(port, backlog);
        default:
            return super.createServerSocket(port, backlog);
        }
    }

    @Override
    public ServerSocket createServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
        switch (type) {
        case SSL_RECEIVER:
            return serverSocketFactory.createServerSocket(port, backlog, bindAddr);
        default:
            return super.createServerSocket(port, backlog, bindAddr);            
        }
    }

    @Override
    public Socket createSocket() {
        switch (type) {
        case SSL_RECEIVER:
            try {
                return socketFactory.createSocket();
            } catch (IOException e) {
                return null;
            }
        default:
            return super.createSocket();
        }
    }

    @Override
    public Socket createResponseSocket() {
        switch (type) {
        case SSL_RECEIVER:
            try {
                return socketFactory.createSocket();
            } catch (IOException e) {
                return null;
            }
        default:
            return super.createResponseSocket();
        }
    }

    @Override
    public Map<String, Object> getSocketInformation(Socket socket) {
        // TODO Return useful socket information

        Map<String, Object> result = super.getSocketInformation(socket);
        result.put("tcsslSSL", type == ConfigType.SSL_RECEIVER);

        return result;
    }

    private enum ConfigType {
        DEFAULT,
        SSL_RECEIVER
    }
}
