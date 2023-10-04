package com.delight.auth.utility;


import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class PasswordUtil {
    @SneakyThrows
    public static String encodePassword(String raw) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(hash);
    }

    public static void main(String[] args) {
        System.out.println(encodePassword("khanhlv@123")
        );
    }
}