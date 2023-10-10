package ru.ylab.in.ui;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

public interface Session {
    boolean isPresent();

    String getUserName();

    Optional<User> getUser();

    Wallet getUserWallet();

    void setUserWallet(Wallet wallet);

    void login(Long userId, String password) throws AccessDeniedException, NotFoundException;

    void logout();
}
