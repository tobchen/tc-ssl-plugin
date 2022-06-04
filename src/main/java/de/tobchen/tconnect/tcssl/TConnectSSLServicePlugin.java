package de.tobchen.tconnect.tcssl;

import java.util.Map;
import java.util.Properties;

import com.mirth.connect.model.ExtensionPermission;
import com.mirth.connect.model.converters.ObjectXMLSerializer;
import com.mirth.connect.model.converters.PluginPropertiesConverter;
import com.mirth.connect.plugins.ServicePlugin;
import com.mirth.connect.server.controllers.ConfigurationController;
import com.mirth.connect.server.controllers.ControllerFactory;
import com.thoughtworks.xstream.XStream;

public class TConnectSSLServicePlugin implements ServicePlugin {

    private ConfigurationController configurationController = ControllerFactory.getFactory().createConfigurationController();

    @Override
    public String getPluginPointName() {
        return "Toby Connect SSL Service Plugin";
    }

    @Override
    public void start() { }

    @Override
    public void stop() { }

    @Override
    public Properties getDefaultProperties() {
        return new Properties();
    }

    @Override
    public ExtensionPermission[] getExtensionPermissions() {
        return null;
    }

    @Override
    public Map<String, Object> getObjectsForSwaggerExamples() {
        return null;
    }

    @Override
    public void init(Properties properties) {
        configurationController.saveProperty("TCP", "tcpConfigurationClass",
                "de.tobchen.tconnect.tcssl.TConnectSSLConfiguration");
        
        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
        XStream xstream = serializer.getXStream();
        xstream.registerLocalConverter(TConnectSSLPluginProperties.class, "connectorPluginProperties", new PluginPropertiesConverter(serializer.getNormalizedVersion(), xstream.getMapper()));
    }

    @Override
    public void update(Properties properties) { }
    
}
