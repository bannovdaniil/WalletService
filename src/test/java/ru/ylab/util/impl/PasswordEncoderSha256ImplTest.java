package ru.ylab.util.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ylab.util.PasswordEncoder;

class PasswordEncoderSha256ImplTest {

    @Test
    void encode() {
        PasswordEncoder passwordEncoder = PasswordEncoderSha256Impl.getInstance();

        String expected = "password";

        String result = passwordEncoder.encode(expected);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isBlank());
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertNotEquals(expected, result);
    }
}