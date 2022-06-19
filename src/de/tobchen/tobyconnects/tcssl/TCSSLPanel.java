package de.tobchen.tobyconnects.tcssl;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.HashSet;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.mirth.connect.client.ui.AbstractConnectorPropertiesPanel;
import com.mirth.connect.client.ui.UIConstants;
import com.mirth.connect.client.ui.components.MirthButton;
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

    private MirthCheckBox trustAllCertsBox;

    private JLabel trustedCertPathsLabel;
    private DefaultListModel<String> trustedCertPathsModel;
    private JList<String> trustedCertPathsList;
    
    private MirthButton newButton;
    private MirthButton deleteButton;

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

        properties.setTrustAllCerts(trustAllCertsBox.isSelected());
        properties.setTrustedCertPaths(new HashSet<>(Collections.list(trustedCertPathsModel.elements())));
        
        return properties;
    }

    @Override
    public void setProperties(ConnectorProperties connectorProperties, ConnectorPluginProperties properties, Mode mode, String transportName) {
        TCSSLPluginProperties sslProperties = (TCSSLPluginProperties) properties;
        
        enabledBox.setSelected(sslProperties.isEnabled());

        certPathField.setText(sslProperties.getCertPath());
        keyPathField.setText(sslProperties.getKeyPath());

        trustAllCertsBox.setSelected(sslProperties.doTrustAllCerts());
        
        trustedCertPathsModel.clear();
        trustedCertPathsModel.addAll(sslProperties.getTrustedCertPaths());

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

        trustAllCertsBox = new MirthCheckBox("Trust All Certificates");
        trustAllCertsBox.setBackground(getBackground());
        trustAllCertsBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateActivations();
            }
        });

        trustedCertPathsLabel = new JLabel("Trusted Certificate Paths:");
        trustedCertPathsModel = new DefaultListModel<>();
        trustedCertPathsList = new JList<>(trustedCertPathsModel);
        trustedCertPathsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        newButton = new MirthButton("New...");
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newTrustedCertClicked();
            }
        });
        deleteButton = new MirthButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTrustedCertClicked();
            }
        });
    }

    private void initToolTips() {
        enabledBox.setToolTipText("Enable or disable SSL.");
        certPathField.setToolTipText("Path to connector X509 certificate PEM file. Required for server mode, optional for client mode.");
        keyPathField.setToolTipText("Path to connector PKCS8 private key PEM file. Only in server mode.");
        trustedCertPathsList.setToolTipText("Paths to trusted X509 certificate PEM files.");
        newButton.setToolTipText("Add new path to trusted certificate list.");
        deleteButton.setToolTipText("Remove selected path from trusted certificate list.");
    }

    private void initLayout() {
        JScrollPane scrollPane = new JScrollPane(trustedCertPathsList);
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(newButton);
        pane.add(deleteButton);

        setLayout(new MigLayout());
        add(enabledBox, "span");
        add(certPathLabel, "newline, right");
        add(certPathField, "w 400");
        add(keyPathLabel, "newline, right");
        add(keyPathField, "w 400");
        add(trustAllCertsBox, "newline, span");
        add(trustedCertPathsLabel, "newline, span");
        add(scrollPane, "newline, span, h 100, w 400");
        add(pane, "newline");
    }

    private void updateActivations() {
        certPathLabel.setEnabled(enabledBox.isSelected());
        certPathField.setEnabled(enabledBox.isSelected());
        keyPathLabel.setEnabled(enabledBox.isSelected());
        keyPathField.setEnabled(enabledBox.isSelected());
        trustAllCertsBox.setEnabled(enabledBox.isSelected());
        trustedCertPathsList.setEnabled(enabledBox.isSelected() && !trustAllCertsBox.isSelected());
        newButton.setEnabled(enabledBox.isSelected() && !trustAllCertsBox.isSelected());
        deleteButton.setEnabled(enabledBox.isSelected() && !trustAllCertsBox.isSelected());
    }

    private void newTrustedCertClicked() {
        String path = (String) JOptionPane.showInputDialog(this, "Trusted Cert:", "New Trusted Cert",
                JOptionPane.PLAIN_MESSAGE, null, null, "");
        if (path != null && trustedCertPathsModel.indexOf(path) < 0) {
            trustedCertPathsModel.addElement(path);
            trustedCertPathsList.setSelectedIndex(trustedCertPathsModel.getSize() - 1);
        }
    }

    private void deleteTrustedCertClicked() {
        int index = trustedCertPathsList.getSelectedIndex();
        if (index >= 0) {
            trustedCertPathsModel.remove(index);
        }
    }
}
