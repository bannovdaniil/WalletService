package ru.ylab.repository;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.Entity;

import java.util.List;
import java.util.Optional;

public interface Repository<T extends Entity, K> {
    T save(T t);

    void update(T t) throws NotFoundException;

    Optional<T> findById(K id);

    List<T> findAll();

    boolean exitsById(K id);
}
