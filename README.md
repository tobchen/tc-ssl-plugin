# Toby Connects SSL Plugin

This plugin enables (or rather, *will* enable) SSL in Mirth Connect TCP connectors.

## Dependencies

Copy dependencies from your Mirth Connect installation to */lib/*:

- *mirth-server.jar* from *Mirth Connect/server-lib/*
- *donkey-server.jar* from *Mirth Connect/server-lib/donkey/*
- *donkey-model.jar* from *Mirth Connect/client-lib/*
- *mirth-client.jar* from *Mirth Connect/client-lib/*
- *tcp-shared.jar* from *Mirth Connect/extensions/tcp/*
- *tcp-server.jar* from *Mirth Connect/extensions/tcp/*

Additionally download to */lib/*:

- *log4j-1.2.16.jar*
- *commons-lang3-3.9.jar*
- *miglayout-core-4.2.jar*
- *miglayout-swing-4.2.jar*
- *xstream-1.4.12.jar*

## Compile And Package

In */*, to create `tcssl.zip` execute: `ant -DsignAlias=<alias> -DsignPass=<password>`

The parameters are needed to sign the JAR. Self-signed archives can be used in Mirth Adminstrator Launcher when run with argumens `-d` and/or `-k`.

## Usage

## To Do

- Store all passwords in `char[]`
- TCP Receiver
  - Receive as Client
- TCP Sender
