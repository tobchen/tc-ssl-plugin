package de.tobchen.tobyconnects.tcssl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.mirth.connect.donkey.model.channel.ConnectorPluginProperties;

/*
 * Very much inspired by HttpAuthConnectorPluginProperties and BasicHttpAuthProperties from Mirth Connect.
 */

public class TCSSLPluginProperties extends ConnectorPluginProperties {

    public static final String PLUGIN_POINT = "Toby Connects SSL Plugin Properties";

    private boolean enabled = false;

    private String certPath = "";
    private String keyPath = "";

    public TCSSLPluginProperties() { }

    public TCSSLPluginProperties(TCSSLPluginProperties pluginProperties) {
        this.enabled = pluginProperties.isEnabled();
        this.certPath = pluginProperties.getCertPath();
        this.keyPath = pluginProperties.getKeyPath();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getCertPath() {
        return certPath;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    @Override
    public Map<String, Object> getPurgedProperties() {
        return new HashMap<>();
    }

    @Override
    public ConnectorPluginProperties clone() {
        return new TCSSLPluginProperties(this);
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
