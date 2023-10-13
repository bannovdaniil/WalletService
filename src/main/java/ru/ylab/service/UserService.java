package ru.ylab.service;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;

import java.util.List;

public interface UserService {
    User add(UserIncomingDto user) throws NotFoundException;

    User findById(Long userId) throws NotFoundException;

    List<User> findAll();
}
