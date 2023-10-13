package ru.ylab.exception;

/**
 * NotFoundException - возникает в случаи отсутствия сущности в базе.
 * <p>
 * Данное исключение сделано checked т.к. пользователю консольного приложения, чтобы не потерять контроль
 * необходимо обработать исключения которые, могут привести к завершению процесса.
 */
public class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }
}
