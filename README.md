# Toby Connect SSL Plugin

## Dependencies

Copy dependencies from your Mirth Connect installation to */lib/*.

- *mirth-server.jar* from *Mirth Connect/server-lib/*
- *donkey-server.jar* from *Mirth Connect/server-lib/donkey/*
- *donkey-model.jar* from *Mirth Connect/client-lib/*
- *mirth-client.jar* from *Mirth Connect/client-lib/*
- *tcp-shared.jar* from *Mirth Connect/extensions/tcp/*
- *tcp-server.jar* from *Mirth Connect/extensions/tcp/*

## Compile And Package

From */* execute: `mvn package`. Put *.jar* and *plugin.xml* in a folder *tcssl* and package as *tcssl.zip*.
