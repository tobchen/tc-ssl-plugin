/*
 * Copyright 2022 Tobias Heukäufer
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

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
import com.mirth.connect.connectors.tcp.TcpReceiver;
import com.mirth.connect.donkey.model.channel.ConnectorPluginProperties;
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
                        if (certPath.isEmpty()) {
                            certPath = null;
                        }
                        if (keyPath.isEmpty()) {
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
