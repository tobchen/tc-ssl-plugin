/*
 * Copyright 2022 Tobias Heukäufer
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.tobchen.tobyconnects.tcssl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

// TODO Use bouncy castle

public class CertAndKeyStore {
    // TODO Have CERTIFIATES be of entries of list of certifactes, so multiple certs per file can be cached
    private final static Map<String, Entry<X509Certificate>> CERTIFICATES = new HashMap<>();
    private final static Map<String, Entry<PrivateKey>> PRIVATE_KEYS = new HashMap<>();

    public static X509Certificate readCertificate(String path) throws IOException, CertificateException {
        Entry<byte[]> readEntry = readFile(path);
        Entry<X509Certificate> certEntry = CERTIFICATES.get(path);

        if (certEntry != null && readEntry.CHECKSUM == certEntry.CHECKSUM) {
            return certEntry.CONTENT;
        }

        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(readEntry.CONTENT));

        CERTIFICATES.put(path, new Entry<>(readEntry.CHECKSUM, cert));

        return cert;
    }

    public static PrivateKey readKey(String path) throws InvalidKeySpecException, IOException, NoSuchAlgorithmException {
        Entry<byte[]> readEntry = readFile(path);
        Entry<PrivateKey> keyEntry = PRIVATE_KEYS.get(path);

        if (keyEntry != null && readEntry.CHECKSUM == keyEntry.CHECKSUM) {
            return keyEntry.CONTENT;
        }

        String key = new String(readEntry.CONTENT)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\n", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = factory.generatePrivate(spec);
        PRIVATE_KEYS.put(path, new Entry<>(readEntry.CHECKSUM, privateKey));

        return privateKey;
    }

    private static Entry<byte[]> readFile(String path) throws IOException {
        byte[] content = Files.readAllBytes(Paths.get(path));

        Checksum checksum = new CRC32();
        checksum.update(content);
        
        return new Entry<>(checksum.getValue(), content);
    }

    private static class Entry<T> {
        private final long CHECKSUM;
        private final T CONTENT;

        public Entry(long checksum, T content) {
            CHECKSUM = checksum;
            CONTENT = content;
        }
    }
}
