package ru.ylab.util.menu;

import ru.ylab.exception.NotFoundException;

import java.nio.file.AccessDeniedException;

public interface Session {
    boolean isPresent();

    String getSessionUserName();

    void login(Long userId, String password) throws AccessDeniedException, NotFoundException;

    void logout();
}
