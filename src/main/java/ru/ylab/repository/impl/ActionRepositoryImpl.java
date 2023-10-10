package ru.ylab.repository.impl;

import ru.ylab.model.Action;
import ru.ylab.repository.ActionRepository;

public final class ActionRepositoryImpl extends RepositoryImpl<Action> implements ActionRepository {
    private static ActionRepository instance;

    public static synchronized ActionRepository getInstance() {
        if (instance == null) {
            instance = new ActionRepositoryImpl();
        }
        return instance;
    }

}
