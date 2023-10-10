package ru.ylab.util.impl;

import ru.ylab.exception.HashAlgorithmException;
import ru.ylab.util.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoderSha256Impl implements PasswordEncoder {
    private static PasswordEncoder instance;
    MessageDigest digest;

    private PasswordEncoderSha256Impl() {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new HashAlgorithmException("Алгоритм шифрования не найден.");
        }
    }

    public static synchronized PasswordEncoder getInstance() {
        if (instance == null) {
            instance = new PasswordEncoderSha256Impl();
        }
        return instance;
    }

    public String encode(String password) {
        byte[] hash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8)
        );
        return new String(hash);
    }

}
