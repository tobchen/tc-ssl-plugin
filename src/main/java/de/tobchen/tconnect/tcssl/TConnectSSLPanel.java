package de.tobchen.tconnect.tcssl;

import java.awt.Component;

import com.mirth.connect.client.ui.AbstractConnectorPropertiesPanel;
import com.mirth.connect.client.ui.UIConstants;
import com.mirth.connect.client.ui.components.MirthCheckBox;
import com.mirth.connect.donkey.model.channel.ConnectorPluginProperties;
import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.model.Connector.Mode;

import net.miginfocom.swing.MigLayout;

/*
 * Very much inspired by HttpAuthConnectorPropertiesPanel from Mirth Connect
 */

public class TConnectSSLPanel extends AbstractConnectorPropertiesPanel {

    private MirthCheckBox enabledBox;

    public TConnectSSLPanel() {
        initComponents();
        initToolTips();
        initLayout();
    }

    @Override
    public ConnectorPluginProperties getProperties() {
        TConnectSSLPluginProperties properties = new TConnectSSLPluginProperties();
        properties.setEnabled(enabledBox.isSelected());
        return properties;
    }

    @Override
    public void setProperties(ConnectorProperties connectorProperties, ConnectorPluginProperties properties, Mode mode, String transportName) {
        TConnectSSLPluginProperties sslProperties = (TConnectSSLPluginProperties) properties;
        enabledBox.setSelected(sslProperties.isEnabled());
    }

    @Override
    public ConnectorPluginProperties getDefaults() {
        return new TConnectSSLPluginProperties();
    }

    @Override
    public boolean checkProperties(ConnectorProperties connectorProperties, ConnectorPluginProperties properties,
            Mode mode, String transportName, boolean highlight) {
        // TODO Actually do check properties
        return true;
    }

    @Override
    public void resetInvalidProperties() {
        // TODO Reset after actually checked
    }

    @Override
    public Component[][] getLayoutComponents() {
        return null;
    }

    @Override
    public void setLayoutComponentsEnabled(boolean enabled) { }
    
    private void initComponents() {
        setBackground(UIConstants.BACKGROUND_COLOR);

        enabledBox = new MirthCheckBox("Enabled");
        enabledBox.setBackground(getBackground());
    }

    private void initToolTips() {
        enabledBox.setToolTipText("Enable or disable SSL.");
    }

    private void initLayout() {
        setLayout(new MigLayout());
        add(enabledBox);
    }
}
