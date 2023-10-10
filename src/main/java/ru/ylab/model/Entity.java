package ru.ylab.model;

/**
 * Базовая сущность для InMemory Model
 */
public abstract class Entity {
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
