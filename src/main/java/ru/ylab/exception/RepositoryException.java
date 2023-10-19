package ru.ylab.exception;

/**
 * RepositoryException - исключения, которые возникают при работе Repository.
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message) {
        super(message);
    }
}
