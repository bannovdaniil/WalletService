package ru.ylab.repository.impl;

import ru.ylab.model.User;
import ru.ylab.repository.UserRepository;

public final class UserRepositoryImpl extends RepositoryImpl<User> implements UserRepository {
    private static UserRepository instance;

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepositoryImpl();
        }
        return instance;
    }

}
