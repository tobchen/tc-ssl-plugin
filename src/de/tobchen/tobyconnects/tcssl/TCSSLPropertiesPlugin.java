/*
 * Copyright 2022 Tobias Heukäufer
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.tobchen.tobyconnects.tcssl;

import com.mirth.connect.client.ui.AbstractConnectorPropertiesPanel;
import com.mirth.connect.connectors.tcp.TcpDispatcherProperties;
import com.mirth.connect.connectors.tcp.TcpReceiverProperties;
import com.mirth.connect.model.converters.ObjectXMLSerializer;
import com.mirth.connect.model.converters.PluginPropertiesConverter;
import com.mirth.connect.plugins.ConnectorPropertiesPlugin;
import com.thoughtworks.xstream.XStream;

/*
 * Very much inspired by HttpAuthConnectorPropertiesPlugin from Mirth Connect by NextGen.
 */

public class TCSSLPropertiesPlugin extends ConnectorPropertiesPlugin {

    public TCSSLPropertiesPlugin(String pluginName) {
        super(pluginName);

        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
        XStream xstream = serializer.getXStream();
        xstream.registerLocalConverter(TCSSLPluginProperties.class, "connectorPluginProperties", new PluginPropertiesConverter(serializer.getNormalizedVersion(), xstream.getMapper()));
    }

    @Override
    public AbstractConnectorPropertiesPanel getConnectorPropertiesPanel() {
        return new TCSSLPanel();
    }

    @Override
    public String getSettingsTitle() {
        return "Toby Connects SSL Settings";
    }

    @Override
    public boolean isConnectorPropertiesPluginSupported(String pluginPointName) {
        return false;
    }

    @Override
    public boolean isSupported(String transportName) {
        return TcpReceiverProperties.NAME.equals(transportName) || TcpDispatcherProperties.NAME.equals(transportName);
    }

    @Override
    public String getPluginPointName() {
        return TCSSLPluginProperties.PLUGIN_POINT;
    }
    
}
