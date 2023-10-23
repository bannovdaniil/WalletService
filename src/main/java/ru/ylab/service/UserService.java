package ru.ylab.service;

import ru.ylab.exception.NotFoundException;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserOutDto;

import java.util.List;

public interface UserService {
    /**
     * Создать нового пользователя.
     *
     * @throws NotFoundException - если при создании пользователя не удалось прикрепить счет.
     */
    UserOutDto add(UserIncomingDto dto) throws NotFoundException;

    /**
     * Найти пользователя по ID
     *
     * @param userId - ID пользователя
     * @return - Пользовательская сущность.
     * @throws NotFoundException - если пользователь не найден
     */
    UserOutDto findById(Long userId) throws NotFoundException;

    /**
     * Вернуть список всех пользователей из базы.
     *
     * @return - список пользователей
     */
    List<UserOutDto> findAll();
}
