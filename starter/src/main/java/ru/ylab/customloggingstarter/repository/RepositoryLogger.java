package ru.ylab.customloggingstarter.repository;

import java.util.List;

/**
 * Основная абстракция работы с логирующими сущностями Action и Transaction.
 *
 * @param <T> тип
 */
public interface RepositoryLogger<T> {
    T save(T t);

    List<T> findAll();
}
