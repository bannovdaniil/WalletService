package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.ylab.TestApplicationConfig;
import ru.ylab.model.Action;
import ru.ylab.repository.ActionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ActionRepositoryImplTest extends TestApplicationConfig {
    private final ActionRepository actionRepository;
    private final PostgreSQLContainer container;

    @BeforeAll
    void beforeAll() {
        container.start();
    }

    @DisplayName("Save Action")
    @Test
    void save() {
        String expectedName = "new Action";
        Action action = new Action(
                LocalDateTime.now(),
                expectedName,
                1L,
                "information");

        Long actionId = actionRepository.save(action).getId();
        List<Action> actionList = actionRepository.findAll();
        Optional<Action> resultAction = actionList.stream().filter(a -> actionId.equals(a.getId())).findFirst();

        Assertions.assertTrue(resultAction.isPresent());
        Assertions.assertEquals(expectedName, resultAction.get().getUserAction());
    }

    @DisplayName("Find All Action")
    @Test
    void findAll() {
        int expectedSize = actionRepository.findAll().size() + 1;
        Action action = new Action(
                LocalDateTime.now(),
                "action Name",
                1L,
                "information");

        actionRepository.save(action);

        int resultSize = actionRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }
}