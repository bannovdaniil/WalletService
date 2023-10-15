package ru.ylab.repository.impl;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab.model.Action;
import ru.ylab.repository.ActionRepository;
import ru.ylab.util.LiquibaseUtil;
import ru.ylab.util.PropertiesUtil;
import ru.ylab.util.impl.ApplicationPropertiesUtilImpl;
import ru.ylab.util.impl.LiquibaseUtilImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Testcontainers
@Tag("DockerRequired")
class ActionRepositoryImplTest {
    private static final int containerPort = 5432;
    private static final int localPort = 54320;
    private static PropertiesUtil propertiesUtil = ApplicationPropertiesUtilImpl.getInstance();
    private static LiquibaseUtil liquibaseUtil = LiquibaseUtilImpl.getInstance();

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("wallet_db")
            .withUsername(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.USERNAME_KEY))
            .withPassword(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.PASSWORD_KEY))
            .withExposedPorts(containerPort)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
            ));
    public static ActionRepository actionRepository;
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        container.start();
        actionRepository = ActionRepositoryImpl.getInstance();
        jdbcDatabaseDelegate = new JdbcDatabaseDelegate(container, "");
    }

    @AfterAll
    static void afterAll() {
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
        Optional<Action> resultAction = actionList.stream().filter(a -> a.getId() == actionId).findFirst();

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