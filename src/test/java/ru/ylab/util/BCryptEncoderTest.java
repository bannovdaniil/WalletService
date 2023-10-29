package ru.ylab.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class BCryptEncoderTest {

    @Test
    void encode() {
        PasswordEncoder passwordEncoder = new BCryptEncoder();

        String expected = "password";

        String result = passwordEncoder.encode(expected);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isBlank());
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertNotEquals(expected, result);
    }


}