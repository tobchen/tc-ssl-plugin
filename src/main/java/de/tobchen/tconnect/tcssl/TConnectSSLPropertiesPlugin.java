package de.tobchen.tconnect.tcssl;

import com.mirth.connect.client.ui.AbstractConnectorPropertiesPanel;
import com.mirth.connect.connectors.tcp.TcpReceiverProperties;
import com.mirth.connect.model.converters.ObjectXMLSerializer;
import com.mirth.connect.model.converters.PluginPropertiesConverter;
import com.mirth.connect.plugins.ConnectorPropertiesPlugin;
import com.thoughtworks.xstream.XStream;

/*
 * Very much inspired by HttpAuthConnectorPropertiesPlugin from Mirth Connect.
 */

public class TConnectSSLPropertiesPlugin extends ConnectorPropertiesPlugin {

    public TConnectSSLPropertiesPlugin(String pluginName) {
        super(pluginName);

        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
        XStream xstream = serializer.getXStream();
        xstream.registerLocalConverter(TConnectSSLPluginProperties.class, "connectorPluginProperties", new PluginPropertiesConverter(serializer.getNormalizedVersion(), xstream.getMapper()));
    }

    @Override
    public AbstractConnectorPropertiesPanel getConnectorPropertiesPanel() {
        return new TConnectSSLPanel();
    }

    @Override
    public String getSettingsTitle() {
        return "Toby Connect SSL Settings";
    }

    @Override
    public boolean isConnectorPropertiesPluginSupported(String pluginPointName) {
        return false;
    }

    @Override
    public boolean isSupported(String transportName) {
        return TcpReceiverProperties.NAME.equals(transportName);
    }

    @Override
    public String getPluginPointName() {
        return TConnectSSLPluginProperties.PLUGIN_POINT;
    }
    
}
