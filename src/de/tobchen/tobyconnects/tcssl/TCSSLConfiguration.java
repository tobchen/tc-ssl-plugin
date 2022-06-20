package de.tobchen.tobyconnects.tcssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.mirth.connect.connectors.tcp.DefaultTcpConfiguration;
import com.mirth.connect.connectors.tcp.TcpDispatcher;
import com.mirth.connect.connectors.tcp.TcpDispatcherProperties;
import com.mirth.connect.connectors.tcp.TcpReceiver;
import com.mirth.connect.connectors.tcp.TcpReceiverProperties;
import com.mirth.connect.donkey.model.channel.ConnectorPluginProperties;
import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.donkey.server.channel.Connector;

public class TCSSLConfiguration extends DefaultTcpConfiguration {

    private ConfigType type = ConfigType.DEFAULT;
    private boolean trustAllCerts = false;

    private SSLSocketFactory socketFactory;
    private SSLServerSocketFactory serverSocketFactory;

    @Override
    public void configureConnectorDeploy(Connector connector) throws Exception {
        super.configureConnectorDeploy(connector);

        type = ConfigType.DEFAULT;

        if (connector instanceof TcpReceiver || connector instanceof TcpDispatcher) {
            for (ConnectorPluginProperties properties : connector.getConnectorProperties().getPluginProperties()) {
                if (properties instanceof TCSSLPluginProperties) {
                    TCSSLPluginProperties sslProperties = (TCSSLPluginProperties) properties;
                    
                    if (sslProperties.isEnabled()) {
                        type = ConfigType.SSL;

                        String certPath = sslProperties.getCertPath();
                        String keyPath = sslProperties.getKeyPath();
                        ConnectorProperties connectorProperties = connector.getConnectorProperties();
                        if ((connectorProperties instanceof TcpReceiverProperties
                                && !((TcpReceiverProperties) connectorProperties).isServerMode())
                                || (connectorProperties instanceof TcpDispatcherProperties
                                && !((TcpDispatcherProperties) connectorProperties).isServerMode())) {
                            if (certPath.isEmpty()) {
                                certPath = null;
                            }
                            keyPath = null;
                        }
                        KeyManager keyManager = new CertAndKeyManager(certPath, keyPath);

                        trustAllCerts = sslProperties.doTrustAllCerts();
                        TrustManager trustManager = TrustAllManager.INSTANCE;
                        if (!trustAllCerts) {
                            trustManager = new TrustSomeManager(sslProperties.getTrustedCertPaths().toArray(new String[0]));
                        }

                        SSLContext context = SSLContext.getInstance("TLS");
                        context.init(new KeyManager[] { keyManager },
                                new TrustManager[] { trustManager },
                                null);
                        
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
        case SSL:
            SSLServerSocket socket = (SSLServerSocket) serverSocketFactory.createServerSocket(port, backlog);
            socket.setNeedClientAuth(!trustAllCerts);
            return socket;
        default:
            return super.createServerSocket(port, backlog);
        }
    }

    @Override
    public ServerSocket createServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
        switch (type) {
        case SSL:
            SSLServerSocket socket = (SSLServerSocket) serverSocketFactory.createServerSocket(port, backlog, bindAddr);
            socket.setNeedClientAuth(!trustAllCerts);
            return socket;
        default:
            return super.createServerSocket(port, backlog, bindAddr);            
        }
    }

    @Override
    public Socket createSocket() {
        switch (type) {
        case SSL:
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
        return super.createResponseSocket();
    }

    @Override
    public Map<String, Object> getSocketInformation(Socket socket) {
        // TODO Return useful socket information

        Map<String, Object> result = super.getSocketInformation(socket);
        result.put("tcsslSSL", type == ConfigType.SSL);

        return result;
    }

    private enum ConfigType {
        DEFAULT,
        SSL
    }
}
