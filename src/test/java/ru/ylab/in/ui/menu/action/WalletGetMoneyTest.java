package ru.ylab.in.ui.menu.action;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab.exception.NotFoundException;
import ru.ylab.in.ui.Session;
import ru.ylab.in.ui.SessionImpl;
import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.service.UserService;
import ru.ylab.service.WalletService;
import ru.ylab.service.impl.UserServiceImpl;
import ru.ylab.service.impl.WalletServiceImpl;
import ru.ylab.util.LiquibaseUtil;
import ru.ylab.util.PropertiesUtil;
import ru.ylab.util.impl.ApplicationPropertiesUtilImpl;
import ru.ylab.util.impl.LiquibaseUtilImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.AccessDeniedException;

@Testcontainers
@Tag("DockerRequired")
class WalletGetMoneyTest {
    private static final int containerPort = 5432;
    private static final int localPort = 54320;
    private static final PropertiesUtil propertiesUtil = ApplicationPropertiesUtilImpl.getInstance();
    private static final LiquibaseUtil liquibaseUtil = LiquibaseUtilImpl.getInstance();

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("wallet_db")
            .withUsername(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.USERNAME_KEY))
            .withPassword(propertiesUtil.getProperties(ApplicationPropertiesUtilImpl.PASSWORD_KEY))
            .withExposedPorts(containerPort)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
            ));

    private final static UserService userService = UserServiceImpl.getInstance();
    private final static WalletService walletService = WalletServiceImpl.getInstance();
    private final static Session session = SessionImpl.getInstance();
    private WalletGetMoney walletGetMoney;
    private InputStream oldSystemIn;
    private PrintStream oldSystemOut;
    private PrintStream oldSystemErr;
    private ByteArrayOutputStream testOut;
    private ByteArrayOutputStream testErr;

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @BeforeEach
    void setUp() throws NotFoundException, AccessDeniedException {
        liquibaseUtil.init();

        User user = userService.add(new UserIncomingDto(
                "First",
                "Last",
                "123"
        ));
        session.login(user.getId(), "123");

        walletGetMoney = new WalletGetMoney();
        oldSystemIn = System.in;

        oldSystemOut = System.out;
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        oldSystemErr = System.err;
        testErr = new ByteArrayOutputStream();
        System.setErr(new PrintStream(testErr));

        walletService.putMoney(user, "1000");
    }

    @AfterEach
    void tearDown() {
        System.setIn(oldSystemIn);
        System.setOut(oldSystemOut);
        System.setErr(oldSystemErr);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1000; 'New balance:'; ''",
            "1000.00; 'New balance:'; ''",
            "1000.0; 'New balance:'; ''",
            "1000.00.00; ''; 'Bad arguments'",
            "10.000; ''; 'Bad arguments'",
            "1000,00; 'New balance:'; ''",
            "1000,0; 'New balance:'; ''",
            "1000,00,00; ''; 'Bad arguments'",
            "10,000; ''; 'Bad arguments'",
            "'-1'; ''; 'Bad arguments'",
            "10.,00; ''; 'Bad arguments'",
            "'10000'; ''; 'Not have same money'",
            "'10000'; ''; 'Not have same money'"
    }, delimiter = ';')
    void execution(String expectedValue, String expectedOutResult, String expectedErrResult) {
        InputStream inputText = new ByteArrayInputStream(expectedValue.getBytes());
        System.setIn(inputText);
        walletGetMoney.execution();

        Assertions.assertTrue(testOut.toString().contains(expectedOutResult));
        Assertions.assertTrue(testErr.toString().contains(expectedErrResult));
    }

}