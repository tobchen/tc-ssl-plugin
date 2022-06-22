/*
 * Copyright 2022 Tobias Heukäufer
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.tobchen.tobyconnects.tcssl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.mirth.connect.donkey.model.channel.ConnectorPluginProperties;

/*
 * Very much inspired by HttpAuthConnectorPluginProperties and BasicHttpAuthProperties from Mirth Connect by NextGen.
 */

public class TCSSLPluginProperties extends ConnectorPluginProperties {

    public static final String PLUGIN_POINT = "Toby Connects SSL Plugin Properties";

    private boolean enabled = false;

    private String certPath = "";
    private String keyPath = "";

    private boolean trustAllCerts = false;
    private Set<String> trustedCertPaths = new HashSet<>();

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

    public boolean doTrustAllCerts() {
        return trustAllCerts;
    }

    public Set<String> getTrustedCertPaths() {
        return Set.copyOf(trustedCertPaths);
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

    public void setTrustAllCerts(boolean trustAllCerts) {
        this.trustAllCerts = trustAllCerts;
    }

    public void setTrustedCertPaths(Set<String> trustedCertPaths) {
        this.trustedCertPaths = Set.copyOf(trustedCertPaths);
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
