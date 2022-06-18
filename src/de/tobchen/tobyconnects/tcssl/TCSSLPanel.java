package de.tobchen.tobyconnects.tcssl;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;

import com.mirth.connect.client.ui.AbstractConnectorPropertiesPanel;
import com.mirth.connect.client.ui.UIConstants;
import com.mirth.connect.client.ui.components.MirthCheckBox;
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

    private JLabel certPathLabel;
    private MirthTextField certPathField;

    private JLabel keyPathLabel;
    private MirthTextField keyPathField;

    public TCSSLPanel() {
        initComponents();
        initToolTips();
        initLayout();
    }

    @Override
    public ConnectorPluginProperties getProperties() {
        TCSSLPluginProperties properties = new TCSSLPluginProperties();

        properties.setEnabled(enabledBox.isSelected());

        properties.setCertPath(certPathField.getText());
        properties.setKeyPath(keyPathField.getText());
        
        return properties;
    }

    @Override
    public void setProperties(ConnectorProperties connectorProperties, ConnectorPluginProperties properties, Mode mode, String transportName) {
        TCSSLPluginProperties sslProperties = (TCSSLPluginProperties) properties;
        
        enabledBox.setSelected(sslProperties.isEnabled());

        certPathField.setText(sslProperties.getCertPath());
        keyPathField.setText(sslProperties.getKeyPath());

        updateActivations();
    }

    @Override
    public ConnectorPluginProperties getDefaults() {
        return new TCSSLPluginProperties();
    }

    @Override
    public boolean checkProperties(ConnectorProperties connectorProperties, ConnectorPluginProperties properties,
            Mode mode, String transportName, boolean highlight) {
        return true;
    }

    @Override
    public void resetInvalidProperties() { }

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
        enabledBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateActivations();
            }
        });

        certPathLabel = new JLabel("Certificate Path:");
        certPathField = new MirthTextField();
        certPathField.setBackground(getBackground());

        keyPathLabel = new JLabel("Key Path:");
        keyPathField = new MirthTextField();
        keyPathField.setBackground(getBackground());  
    }

    private void initToolTips() {
        enabledBox.setToolTipText("Enable or disable SSL.");
    }

    private void initLayout() {
        setLayout(new MigLayout());
        add(enabledBox, "span");
        add(certPathLabel, "newline, right");
        add(certPathField, "w 400");
        add(keyPathLabel, "newline, right");
        add(keyPathField, "w 400");
    }

    private void updateActivations() {
        certPathLabel.setEnabled(enabledBox.isEnabled());
        certPathField.setEnabled(enabledBox.isEnabled());
        keyPathLabel.setEnabled(enabledBox.isEnabled());
        keyPathField.setEnabled(enabledBox.isEnabled());
    }
}
