package ru.ylab.service;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.ylab.model.Action;
import ru.ylab.repository.ActionRepository;
import ru.ylab.repository.impl.ActionRepositoryImpl;
import ru.ylab.service.impl.ActionServiceImpl;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

class ActionServiceTest {
    private static ActionRepository mockActionRepository;
    private static ActionService actionService;

    private static void setMock(ActionRepository mock) {
        try {
            Field instance = ActionRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockActionRepository = Mockito.mock(ActionRepository.class);
        setMock(mockActionRepository);
        actionService = ActionServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = ActionRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockActionRepository);
    }

    @Test
    void add() {
        Long expectedId = 1L;

        Action action = new Action(expectedId, LocalDateTime.now(), "Login", null, "information");

        Mockito.doReturn(action).when(mockActionRepository).save(Mockito.any(Action.class));

        Action result = actionService.add(action);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void findAll() {
        actionService.findAll();
        Mockito.verify(mockActionRepository).findAll();
    }
}