package org.tdos.tdospractice.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Security;

public class HashUtils {

    public static final byte[] EMPTY_DATA_HASH;

    private static final String HASH_256_ALGORITHM_NAME = "SHA3-256";

    static {
        Security.addProvider(new BouncyCastleProvider());
        EMPTY_DATA_HASH = sha3(new byte[0]);
    }

    private static byte[] hash(byte[] in, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm, "BC");
            return digest.digest(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] sha3(byte[] input) {
        return hash(input, HASH_256_ALGORITHM_NAME);
    }

    public static String encodesha3(String per) {
        return Hex.toHexString(HashUtils.sha3(per.getBytes(StandardCharsets.UTF_8)));
    }
}