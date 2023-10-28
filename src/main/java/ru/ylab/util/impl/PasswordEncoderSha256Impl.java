package ru.ylab.util.impl;

import org.springframework.stereotype.Component;
import ru.ylab.exception.HashAlgorithmException;
import ru.ylab.util.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Реализация шифрования пароля с помощью sha256
 */
@Component
public class PasswordEncoderSha256Impl implements PasswordEncoder {
    MessageDigest digest;

    public PasswordEncoderSha256Impl() {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new HashAlgorithmException("Алгоритм шифрования не найден.");
        }
    }

    public String encode(String password) {
        byte[] hash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8)
        );
        return new String(hash);
    }

}
