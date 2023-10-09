package ru.ylab.repository.impl;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.Entity;
import ru.ylab.repository.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.*;

public class InMemoryRepositoryImpl<T extends Entity> implements Repository<T, Long> {
    private final Class<T> clazz;

    private Map<Long, T> daoList = new HashMap<>();
    private Long index;

    @SuppressWarnings("unchecked")
    public InMemoryRepositoryImpl() {
        index = 1L;
        this.clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Override
    public T save(T t) {
        t.setId(index);
        daoList.put(index, t);
        index++;
        return t;
    }

    @Override
    public void update(T t) throws NotFoundException {
        checkExistByid(t.getId());
        daoList.put(t.getId(), t);
    }

    @Override
    public void deleteById(Long id) throws NotFoundException {
        checkExistByid(id);
        daoList.remove(id);
    }

    @Override
    public Optional<T> findById(Long id) {
        return exitsById(id) ?
                Optional.of(daoList.get(id)) :
                Optional.empty();
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(daoList.values());
    }

    @Override
    public boolean exitsById(Long id) {
        return daoList.containsKey(id);
    }

    private void checkExistByid(Long id) throws NotFoundException {
        if (!exitsById(id)) {
            throw new NotFoundException(clazz.getName() + " not found");
        }
    }
}
