package de.tobchen.tobyconnects.tcssl;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509KeyManager;

public class SpecificKeyManager implements X509KeyManager {

    private final KeyStore keyStore;

    private final String serverAlias;
    private final char[] serverAliasPassword;
    private final String serverAliasPublicAlgorithm;

    private final String clientAlias;
    private final char[] clientAliasPassword;
    private final String clientAliasPublicAlgorithm;

    public SpecificKeyManager(File keyStoreFile, char[] keyStorePassword,
            String serverAlias, char[] serverAliasPassword, String clientAlias, char[] clientAliasPassword)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        keyStore = KeyStore.getInstance(keyStoreFile, keyStorePassword);

        this.serverAlias = serverAlias;
        this.serverAliasPassword = serverAliasPassword != null ? serverAliasPassword.clone() : null;
        this.serverAliasPublicAlgorithm = getPublicKeyAlgorithm(this.serverAlias);

        this.clientAlias = clientAlias;
        this.clientAliasPassword = clientAliasPassword != null ? clientAliasPassword.clone() : null;
        this.clientAliasPublicAlgorithm = getPublicKeyAlgorithm(this.clientAlias);
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return keyType != null && keyType.equals(clientAliasPublicAlgorithm) ?
                new String[] { clientAlias } : new String[0];
    }

    @Override
    public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
        for (String type : keyTypes) {
            if (type != null && type.equals(clientAliasPublicAlgorithm)) {
                return clientAlias;
            }
        }
        return null;
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return keyType != null && keyType.equals(serverAliasPublicAlgorithm) ?
                new String[] { serverAlias } : new String[0];
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        return keyType != null && keyType.equals(serverAliasPublicAlgorithm) ? serverAlias : null;
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        Certificate[] certs = null;
        try {
            certs = keyStore.getCertificateChain(alias);
        } catch (KeyStoreException e) {
            // TODO Log4j
            e.printStackTrace();
        }

        if (certs != null) {
            X509Certificate[] result = new X509Certificate[certs.length];
            for (int i = 0; i < certs.length; ++i) {
                result[i] = (X509Certificate) certs[i];
            }
            return result;
        } else {
            return null;
        }
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        try {
            if (alias != null) {
                if (alias.equals(serverAlias)) {
                    return (PrivateKey) keyStore.getKey(serverAlias, serverAliasPassword);
                } else if (alias.equals(clientAlias)) {
                    return (PrivateKey) keyStore.getKey(clientAlias, clientAliasPassword);
                }
            }
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            // TODO Log4j
            e.printStackTrace();
        }

        return null;
    }

    private String getPublicKeyAlgorithm(String alias) {
        Certificate cert;
        try {
            cert = alias != null ? keyStore.getCertificate(alias) : null;
        } catch (KeyStoreException e) {
            return null;
        }
        return cert != null ? cert.getPublicKey().getAlgorithm() : null;
    }
}
