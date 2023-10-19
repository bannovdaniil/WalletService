package ru.ylab.in.ui.menu.action;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab.model.User;
import ru.ylab.service.UserService;
import ru.ylab.service.impl.UserServiceImpl;
import ru.ylab.util.LiquibaseUtil;
import ru.ylab.util.PropertiesUtil;
import ru.ylab.util.impl.ApplicationPropertiesUtilImpl;
import ru.ylab.util.impl.LiquibaseUtilImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

@Testcontainers
@Tag("DockerRequired")
class UserRegistrationTest {
    private static final int containerPort = 5432;
    private static final int localPort = 54320;
    private static final PropertiesUtil propertiesUtil = ApplicationPropertiesUtilImpl.getInstance();
    private static final LiquibaseUtil liquibaseUtil = LiquibaseUtilImpl.getInstance();
    private final static UserService userService = UserServiceImpl.getInstance();
    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("wallet_db")
            .withUsername(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.USERNAME_KEY))
            .withPassword(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.PASSWORD_KEY))
            .withExposedPorts(containerPort)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
            ));
    private InputStream oldSystemIn;
    private PrintStream oldSystemOut;
    private PrintStream oldSystemErr;
    private UserRegistration userRegistration;

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        liquibaseUtil.init();
        userRegistration = new UserRegistration();
        oldSystemIn = System.in;

        oldSystemOut = System.out;
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        oldSystemErr = System.err;
        ByteArrayOutputStream testErr = new ByteArrayOutputStream();
        System.setErr(new PrintStream(testErr));
    }

    @AfterEach
    void tearDown() {
        System.setIn(oldSystemIn);
        System.setOut(oldSystemOut);
        System.setErr(oldSystemErr);
    }

    @Test
    void execution() {
        List<User> userList = userService.findAll();
        int beforeSize = userList.size();

        InputStream inputText = new ByteArrayInputStream("First Name\nLast name\npassword\n".getBytes());
        System.setIn(inputText);
        userRegistration.execution();

        userList = userService.findAll();
        int afterSize = userList.size();

        Assertions.assertNotEquals(beforeSize, afterSize);
    }
}