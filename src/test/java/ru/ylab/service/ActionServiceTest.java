package ru.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.model.Action;
import ru.ylab.repository.ActionRepository;
import ru.ylab.service.impl.ActionServiceImpl;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class ActionServiceTest {
    @Mock
    private ActionRepository mockActionRepository;
    @InjectMocks
    private ActionServiceImpl actionService;

    @BeforeEach
    void setUp() {
        Mockito.reset(mockActionRepository);
    }

    @DisplayName("Add action")
    @Test
    void add() {
        Long expectedId = 1L;

        Action action = new Action(expectedId, LocalDateTime.now(), "Login", null, "information");

        Mockito.doReturn(action).when(mockActionRepository).save(Mockito.any(Action.class));

        Action result = actionService.add(action);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @DisplayName("Find all action")
    @Test
    void findAll() {
        actionService.findAll();
        Mockito.verify(mockActionRepository).findAll();
    }
}