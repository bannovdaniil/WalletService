package ru.ylab.repository;

import ru.ylab.model.Entity;

import java.util.List;

/**
 * Основная абстракция работы с логирующими сущностями Action и Transaction.
 *
 * @param <T> тип
 */
public interface RepositoryLogger<T extends Entity> {
    T save(T t);

    List<T> findAll();
}
