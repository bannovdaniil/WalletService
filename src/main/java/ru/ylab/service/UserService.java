package ru.ylab.service;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.User;
import ru.ylab.model.dto.UserIncomingDto;

import java.util.List;

public interface UserService {
    /**
     * Создать нового пользователя.
     * @param dto
     * @return
     * @throws NotFoundException - если при создании пользователя не удалось прикрепить счет.
     */
    User add(UserIncomingDto dto) throws NotFoundException;

    /**
     * Найти пользователя по ID
     * @param userId
     * @return - Пользовательская сущность.
     * @throws NotFoundException - если пользователь не найден
     */
    User findById(Long userId) throws NotFoundException;

    /**
     * Вернуть список всех пользователей из базы.
     * @return
     */
    List<User> findAll();
}
