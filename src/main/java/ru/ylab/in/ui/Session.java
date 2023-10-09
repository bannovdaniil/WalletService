package ru.ylab.in.ui;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.Wallet;

import java.nio.file.AccessDeniedException;

public interface Session {
    boolean isPresent();

    String getUserName();

    Wallet getUserWallet();

    void login(Long userId, String password) throws AccessDeniedException, NotFoundException;

    void logout();
}
