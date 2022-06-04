package de.tobchen.tconnect.tcssl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.mirth.connect.donkey.model.channel.ConnectorPluginProperties;

/*
 * Very much inspired by HttpAuthConnectorPluginProperties and BasicHttpAuthProperties from Mirth Connect.
 */

public class TConnectSSLPluginProperties extends ConnectorPluginProperties {

    public static final String PLUGIN_POINT = "Toby Connect SSL Plugin Properties";

    private boolean enabled;

    public TConnectSSLPluginProperties() {
        enabled = false;
    }

    public TConnectSSLPluginProperties(TConnectSSLPluginProperties tConnectSSLPluginProperties) {
        this.enabled = tConnectSSLPluginProperties.isEnabled();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Map<String, Object> getPurgedProperties() {
        return new HashMap<>();
    }

    @Override
    public ConnectorPluginProperties clone() {
        return new TConnectSSLPluginProperties(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String getName() {
        return PLUGIN_POINT;
    }
    
}
