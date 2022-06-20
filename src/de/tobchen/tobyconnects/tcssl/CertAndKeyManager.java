package de.tobchen.tobyconnects.tcssl;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

import javax.net.ssl.X509KeyManager;

public class CertAndKeyManager implements X509KeyManager {
    private static final String ALIAS = "tobywoby";

    private final X509Certificate CERT;
    private final PrivateKey KEY;

    public CertAndKeyManager(String certPath, String keyPath)
            throws CertificateException, IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        CERT = certPath != null ? CertAndKeyStore.readCertificate(certPath) : null;
        KEY = keyPath != null ? CertAndKeyStore.readKey(keyPath) : null;
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return CERT != null && CERT.getPublicKey().getAlgorithm().equals(keyType) ? new String[] { ALIAS } : null;
    }

    @Override
    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        for (String type : keyType) {
            if (CERT != null && CERT.getPublicKey().getAlgorithm().equals(type)) {
                return ALIAS;
            }
        }
        return null;
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return CERT != null && CERT.getPublicKey().getAlgorithm().equals(keyType) ? new String[] { ALIAS } : null;
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        return CERT != null && CERT.getPublicKey().getAlgorithm().equals(keyType) ? ALIAS : null;
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        return CERT != null && ALIAS.equals(alias) ? new X509Certificate[] { CERT } : null;
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        return KEY != null && ALIAS.equals(alias) ? KEY : null;
    }
    
}
