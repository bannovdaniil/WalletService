package ru.ylab.service.impl;

import ru.ylab.model.Action;
import ru.ylab.repository.ActionRepository;
import ru.ylab.repository.impl.ActionRepositoryImpl;
import ru.ylab.service.ActionService;

import java.util.List;

/**
 * Бизнес логика Action Событий которые делает пользователь.
 */
public class ActionServiceImpl implements ActionService {
    private static ActionService instance;
    private ActionRepository actionRepository = ActionRepositoryImpl.getInstance();

    private ActionServiceImpl() {
    }

    public static synchronized ActionService getInstance() {
        if (instance == null) {
            instance = new ActionServiceImpl();
        }
        return instance;
    }

    @Override
    public Action add(Action action) {
        return actionRepository.save(action);
    }

    @Override
    public List<Action> findAll() {
        return actionRepository.findAll();
    }
}
