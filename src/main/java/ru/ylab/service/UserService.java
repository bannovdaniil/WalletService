package ru.ylab.service;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {
    User add(UserIncomingDto user) throws NotFoundException;

    User find(Long userId) throws NotFoundException;

    void update(User user) throws NotFoundException, AccessDeniedException;

    void delete(Long userId) throws NotFoundException;

    List<User> findAll();
}
