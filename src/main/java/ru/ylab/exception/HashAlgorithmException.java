package ru.ylab.exception;

/**
 * Исключение для поглощения NoSuchAlgorithmException.
 * Может возникнуть в PasswordEncoderSha256Impl
 * в случаи отсутствии алгоритма шифрования sha-256.
 * <p>
 * Используется unchecked с целью поглощения. Исключение необходимо обрабатывать на уровне установки алгоритма
 * и если такой возможности нет, продолжение работы скрипта не имеет смысла, т.к. пользователи не смогут произвести
 * никаких действий в системе, поскольку просто не смогут залогиниться.
 */
public class HashAlgorithmException extends RuntimeException {
    public HashAlgorithmException(String message) {
        super(message);
    }
}
