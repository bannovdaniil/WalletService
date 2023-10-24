package ru.ylab.validator.impl;

import ru.ylab.model.dto.UserLoginDto;
import ru.ylab.validator.Validator;

/**
 * {@inheritDoc}
 */
public class UserLoginDtoValidatorImpl implements Validator<UserLoginDto> {
    private static Validator<UserLoginDto> instance;

    private UserLoginDtoValidatorImpl() {
    }

    public static synchronized Validator<UserLoginDto> getInstance() {
        if (instance == null) {
            instance = new UserLoginDtoValidatorImpl();
        }
        return instance;
    }

    /**
     * Проверка на Null
     * Проверка Входящего числа.
     * Проверка пароля.
     */
    @Override
    public boolean isValid(UserLoginDto dto) {

        return dto != null
               && dto.getUserId() != null
               && dto.getUserId() > 0
               && isNotBlank(dto.getPassword());
    }

    private static boolean isNotBlank(String s) {
        return s != null
               && !s.isEmpty()
               && !s.isBlank();
    }
}
