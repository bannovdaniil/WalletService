package ru.ylab.in.ui.menu.action;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.ylab.exception.NotFoundException;
import ru.ylab.in.ui.Session;
import ru.ylab.in.ui.SessionImpl;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.service.UserService;
import ru.ylab.service.impl.UserServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.AccessDeniedException;

class WalletPutMoneyTest {
    private final static UserService userService = UserServiceImpl.getInstance();
    private final static Session session = SessionImpl.getInstance();
    private WalletPutMoney walletPutMoney;
    private InputStream oldSystemIn;
    private PrintStream oldSystemOut;
    private PrintStream oldSystemErr;
    private ByteArrayOutputStream testOut;
    private ByteArrayOutputStream testErr;

    @BeforeAll
    static void beforeAll() throws NotFoundException, AccessDeniedException {
        userService.add(new UserIncomingDto(
                "First",
                "Last",
                "123"
        ));

        session.login(1L, "123");
    }

    @BeforeEach
    void setUp() {
        walletPutMoney = new WalletPutMoney();
        oldSystemIn = System.in;

        oldSystemOut = System.out;
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        oldSystemErr = System.err;
        testErr = new ByteArrayOutputStream();
        System.setErr(new PrintStream(testErr));
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
            "1000.00.00; ''; 'Bad enter.'",
            "10.000; ''; 'Bad enter.'",
            "1000,00; 'New balance:'; ''",
            "1000,0; 'New balance:'; ''",
            "1000,00,00; ''; 'Bad enter.'",
            "10,000; ''; 'Bad enter.'",
            "'-1'; ''; 'Bad enter.'",
            "10.,00; ''; 'Bad enter.'"
    }, delimiter = ';')
    void execution(String expectedValue, String expectedOutResult, String expectedErrResult) {
        InputStream inputText = new ByteArrayInputStream(expectedValue.getBytes());
        System.setIn(inputText);
        walletPutMoney.execution();

        Assertions.assertTrue(testOut.toString().contains(expectedOutResult));
        Assertions.assertTrue(testErr.toString().contains(expectedErrResult));
    }
}