package ru.ylab.repository;

import ru.ylab.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Основная абстракция работы с базой.
 *
 * @param <T> тип
 * @param <K> тип ID
 */
public interface Repository<T, K> {
    T save(T t);

    void update(T t) throws NotFoundException;

    Optional<T> findById(K id);

    List<T> findAll();

    boolean exitsById(K id);
}
