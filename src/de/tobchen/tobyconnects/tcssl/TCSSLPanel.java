package de.tobchen.tobyconnects.tcssl;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import com.mirth.connect.client.ui.AbstractConnectorPropertiesPanel;
import com.mirth.connect.client.ui.UIConstants;
import com.mirth.connect.client.ui.components.MirthCheckBox;
import com.mirth.connect.client.ui.components.MirthPasswordField;
import com.mirth.connect.client.ui.components.MirthTextField;
import com.mirth.connect.donkey.model.channel.ConnectorPluginProperties;
import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.model.Connector.Mode;

import net.miginfocom.swing.MigLayout;

/*
 * Very much inspired by HttpAuthConnectorPropertiesPanel from Mirth Connect
 */

public class TCSSLPanel extends AbstractConnectorPropertiesPanel {

    private MirthCheckBox enabledBox;

    private JLabel keyStorePathLabel;
    private MirthTextField keyStorePathField;
    private JLabel keyStorePasswordLabel;
    private MirthPasswordField keyStorePasswordField;

    private JLabel connectorAliasLabel;
    private MirthTextField connectorAliasField;
    private JLabel connectorAliasPasswordLabel;
    private MirthPasswordField connectorAliasPasswordField;

    public TCSSLPanel() {
        initComponents();
        initToolTips();
        initLayout();
    }

    @Override
    public ConnectorPluginProperties getProperties() {
        TCSSLPluginProperties properties = new TCSSLPluginProperties();

        properties.setEnabled(enabledBox.isSelected());

        properties.setKeyStorePath(keyStorePathField.getText());
        properties.setKeyStorePassword(new String(keyStorePasswordField.getPassword()));

        properties.setConnectorAlias(connectorAliasField.getText());
        properties.setConnectorAliasPassword(new String(connectorAliasPasswordField.getPassword()));
        
        return properties;
    }

    @Override
    public void setProperties(ConnectorProperties connectorProperties, ConnectorPluginProperties properties, Mode mode, String transportName) {
        TCSSLPluginProperties sslProperties = (TCSSLPluginProperties) properties;
        
        enabledBox.setSelected(sslProperties.isEnabled());

        keyStorePathField.setText(sslProperties.getKeyStorePath());
        keyStorePasswordField.setText(sslProperties.getKeyStorePassword());

        connectorAliasField.setText(sslProperties.getConnectorAlias());
        connectorAliasPasswordField.setText(sslProperties.getConnectorAliasPassword());

        updateActivations();
    }

    @Override
    public ConnectorPluginProperties getDefaults() {
        return new TCSSLPluginProperties();
    }

    @Override
    public boolean checkProperties(ConnectorProperties connectorProperties, ConnectorPluginProperties properties,
            Mode mode, String transportName, boolean highlight) {
        // TODO Actually do check properties
        // TODO Check if key store exists
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
        enabledBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateActivations();
            }
        });

        keyStorePathLabel = new JLabel("Key Store Path:");
        keyStorePathField = new MirthTextField();
        keyStorePathField.setBackground(getBackground());

        keyStorePasswordLabel = new JLabel("Key Store Password:");
        keyStorePasswordField = new MirthPasswordField();
        keyStorePasswordField.setBackground(getBackground());

        connectorAliasLabel = new JLabel("Connector Alias:");
        connectorAliasField = new MirthTextField();
        connectorAliasField.setBackground(getBackground());

        connectorAliasPasswordLabel = new JLabel("Connector Alias Password:");
        connectorAliasPasswordField = new MirthPasswordField();
        connectorAliasPasswordField.setBackground(getBackground());     
    }

    private void initToolTips() {
        enabledBox.setToolTipText("Enable or disable SSL.");
    }

    private void initLayout() {
        setLayout(new MigLayout());
        add(enabledBox, "span");
        add(keyStorePathLabel, "newline, right");
        add(keyStorePathField, "w 400");
        add(keyStorePasswordLabel, "newline, right");
        add(keyStorePasswordField, "w 200");
        add(connectorAliasLabel, "newline, right");
        add(connectorAliasField, "w 150");
        add(connectorAliasPasswordLabel, "newline, right");
        add(connectorAliasPasswordField, "w 200");
    }

    private void updateActivations() {
        keyStorePathLabel.setEnabled(enabledBox.isEnabled());
        keyStorePathField.setEnabled(enabledBox.isEnabled());
        keyStorePasswordLabel.setEnabled(enabledBox.isEnabled());
        keyStorePasswordField.setEnabled(enabledBox.isEnabled());
        connectorAliasLabel.setEnabled(enabledBox.isEnabled());
        connectorAliasField.setEnabled(enabledBox.isEnabled());
        connectorAliasPasswordLabel.setEnabled(enabledBox.isEnabled());
        connectorAliasPasswordField.setEnabled(enabledBox.isEnabled());
    }
}
