package de.tobchen.tobyconnects.tcssl;

import java.net.Socket;
import java.util.Map;

import com.mirth.connect.connectors.tcp.DefaultTcpConfiguration;

public class TCSSLConfiguration extends DefaultTcpConfiguration {
    @Override
    public Map<String, Object> getSocketInformation(Socket socket) {
        Map<String, Object> result = super.getSocketInformation(socket);
        result.put("tobyInfo", "Here's some Toby Info!");

        return result;
    }
}
