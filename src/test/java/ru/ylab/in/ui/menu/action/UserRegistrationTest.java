package ru.ylab.in.ui.menu.action;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.service.UserService;
import ru.ylab.service.impl.UserServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

class UserRegistrationTest {
    private final static UserService userService = UserServiceImpl.getInstance();
    private InputStream oldSystemIn;
    private PrintStream oldSystemOut;
    private PrintStream oldSystemErr;
    private ByteArrayOutputStream testOut;
    private ByteArrayOutputStream testErr;
    private UserRegistration userRegistration;

    @BeforeEach
    void setUp() throws NotFoundException {
        userRegistration = new UserRegistration();
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

    @Test
    void execution() throws NotFoundException {
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