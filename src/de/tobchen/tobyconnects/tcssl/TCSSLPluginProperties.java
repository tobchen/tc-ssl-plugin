package de.tobchen.tobyconnects.tcssl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.mirth.connect.donkey.model.channel.ConnectorPluginProperties;

/*
 * Very much inspired by HttpAuthConnectorPluginProperties and BasicHttpAuthProperties from Mirth Connect.
 */

public class TCSSLPluginProperties extends ConnectorPluginProperties {

    public static final String PLUGIN_POINT = "Toby Connect SSL Plugin Properties";

    private boolean enabled = false;

    private String keyStorePath = "";
    private String keyStorePassword = "";

    private String connectorAlias = "";
    private String connectorAliasPassword = "";

    public TCSSLPluginProperties() { }

    public TCSSLPluginProperties(TCSSLPluginProperties pluginProperties) {
        this.enabled = pluginProperties.isEnabled();
        this.keyStorePath = pluginProperties.getKeyStorePath();
        this.keyStorePassword = pluginProperties.getKeyStorePassword();
        this.connectorAlias = pluginProperties.getConnectorAlias();
        this.connectorAliasPassword = pluginProperties.getConnectorAliasPassword();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public String getConnectorAlias() {
        return connectorAlias;
    }

    public String getConnectorAliasPassword() {
        return connectorAliasPassword;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public void setConnectorAlias(String connectorAlias) {
        this.connectorAlias = connectorAlias;
    }

    public void setConnectorAliasPassword(String connectorAliasPassword) {
        this.connectorAliasPassword = connectorAliasPassword;
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
