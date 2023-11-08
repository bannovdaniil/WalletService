package ru.ylab.customloggingstarter.exception;

/**
 * DataBaseDriverLoadException - Возникает если не удалось загрузить драйвер JDBC.
 */
public class DataBaseDriverLoadException extends RuntimeException {
    public DataBaseDriverLoadException(String message) {
        super(message);
    }
}
