package ru.ylab.repository.impl;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab.Constants;
import ru.ylab.TestApplicationConfig;
import ru.ylab.model.Action;
import ru.ylab.repository.ActionRepository;
import ru.ylab.util.LiquibaseUtil;
import ru.ylab.util.PropertiesUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Testcontainers
@Tag("DockerRequired")
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ContextConfiguration(classes = TestApplicationConfig.class)
class ActionRepositoryImplTest {
    private static final int containerPort = 5432;
    private static final int localPort = 54321;
    private final PropertiesUtil propertiesUtil;
    private final LiquibaseUtil liquibaseUtil;
    private final ActionRepository actionRepository;

    @Container
    public PostgreSQLContainer<?> container;

    @BeforeAll
    void beforeAll() {
        container = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("wallet_db")
                .withUsername(propertiesUtil.getProperties(Constants.USERNAME_KEY, "test"))
                .withPassword(propertiesUtil.getProperties(Constants.PASSWORD_KEY, "test"))
                .withExposedPorts(containerPort)
                .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                        new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
                ));
        System.setProperty("DATASOURCE_URL", "jdbc:postgresql://localhost:" + localPort + "/wallet_db");
        container.start();
    }

    @AfterAll
    void afterAll() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        liquibaseUtil.init();
    }

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