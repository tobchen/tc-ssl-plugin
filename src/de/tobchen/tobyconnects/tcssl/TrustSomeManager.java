/*
 * Copyright 2022 Tobias Heukäufer
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.tobchen.tobyconnects.tcssl;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class TrustSomeManager implements X509TrustManager {
    
    private final X509Certificate[] CERTS;

    public TrustSomeManager(String[] paths) throws CertificateException, IOException {
        CERTS = new X509Certificate[paths.length];
        for (int i = 0; i < paths.length; ++i) {
            CERTS[i] = CertAndKeyStore.readCertificate(paths[i]);
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for (X509Certificate clientCert : chain) {
            for (X509Certificate trustedCert : CERTS) {
                if (clientCert.equals(trustedCert)) {
                    return;
                }
            }
        }
        throw new CertificateException("Client chain has no trusted certificate!");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for (X509Certificate clientCert : chain) {
            for (X509Certificate trustedCert : CERTS) {
                if (clientCert.equals(trustedCert)) {
                    return;
                }
            }
        }
        throw new CertificateException("Server chain has no trusted certificate!");
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
    
}
