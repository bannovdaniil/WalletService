package ru.ylab.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ylab.model.Action;
import ru.ylab.repository.ActionRepository;
import ru.ylab.service.ActionService;

import java.util.List;

/**
 * Бизнес логика Action Событий которые делает пользователь.
 */
@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {
    private final ActionRepository actionRepository;

    @Override
    public Action add(Action action) {
        return actionRepository.save(action);
    }

    @Override
    public List<Action> findAll() {
        return actionRepository.findAll();
    }
}
