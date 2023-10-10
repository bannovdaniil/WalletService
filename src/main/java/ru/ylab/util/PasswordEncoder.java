package ru.ylab.util;

/**
 * Обеспечивает шифрование пароля и составления хеш функции.
 */
public interface PasswordEncoder {
    String encode(String password);
}
