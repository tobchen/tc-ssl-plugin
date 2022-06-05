# Toby Connects SSL Plugin

This plugin enables (or rather, *will* enable) SSL in TCP connectors.

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

The *.jar* will need to be signed. Self-signed archives can be used in Mirth Adminstrator Launcher run with argumens `-d` and/or `-k`.
