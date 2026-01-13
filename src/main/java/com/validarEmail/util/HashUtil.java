package com.validarEmail.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class HashUtil {

    private static final String ALGORITHM = "SHA-256";
    private static final int API_KEY_LENGTH = 32;

    public String generateApiKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[API_KEY_LENGTH];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public String hashApiKey(String apiKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = digest.digest(apiKey.getBytes(StandardCharsets.UTF_8));
            return "SHA256:" + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing API key", e);
        }
    }

    public boolean verifyApiKey(String apiKey, String hash) {
        String computedHash = hashApiKey(apiKey);
        return computedHash.equals(hash);
    }
}
