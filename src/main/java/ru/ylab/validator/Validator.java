package ru.ylab.validator;

/**
 * Проверяет соответствие сущности критериям.
 */
public interface Validator<T> {
    boolean isValid(T t);
}
