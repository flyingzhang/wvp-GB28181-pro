package com.genersoft.iot.vmp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class LogonUtils {
    public static String SHORT_TOKEN_PREFIX = "short-token:";

    public static String generateShortToken()  {
        try {
            // Generate a random UUID
            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString().replace("-", "");

            // Use MD5 hashing to shorten the UUID
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(uuidString.getBytes());
            byte[] digest = md.digest();

            // Convert the hash to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }

            // Return the first 8 characters of the hash
            return sb.toString().substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
}
