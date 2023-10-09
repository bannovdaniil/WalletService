package ru.ylab.util.menu;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.service.UserService;
import ru.ylab.service.impl.UserServiceImpl;
import ru.ylab.util.PasswordEncoder;
import ru.ylab.util.impl.PasswordEncoderSha256Impl;

import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;
import java.util.Optional;

public class SessionImpl implements Session {
    private static Session instance;
    private Optional<User> loggedUser = Optional.empty();
    private final PasswordEncoder passwordEncoder = PasswordEncoderSha256Impl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();

    private SessionImpl() {
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new SessionImpl();
        }
        return instance;
    }

    @Override
    public boolean isPresent() {
        return loggedUser.isPresent();
    }

    @Override
    public String getSessionUserName() {
        if (loggedUser.isPresent()) {
            return String.format("Login as: %s %s", loggedUser.get().getFirstName(), loggedUser.get().getLastName());
        } else {
            return "User not login.";
        }
    }

    @Override
    public void login(Long userId, String password) throws AccessDeniedException, NotFoundException {
        loggedUser = Optional.empty();
        if (userId == null || password == null) {
            throw new InvalidParameterException("Wrong Password or UserId.");
        }
        User user = userService.find(userId);
        if (passwordEncoder.encode(password).equals(user.getHashPassword())) {
            loggedUser = Optional.of(user);
        } else {
            throw new AccessDeniedException("Wrong Password or UserId.");
        }
    }

    @Override
    public void logout() {
        this.loggedUser = Optional.empty();
    }
}
