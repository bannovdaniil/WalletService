package ru.ylab.validator.impl;

import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.validator.Validator;

/**
 * {@inheritDoc}
 */
public class UserIncomingDtoValidatorImpl implements Validator<UserIncomingDto> {
    private static Validator<UserIncomingDto> instance;

    private UserIncomingDtoValidatorImpl() {
    }

    public static synchronized Validator<UserIncomingDto> getInstance() {
        if (instance == null) {
            instance = new UserIncomingDtoValidatorImpl();
        }
        return instance;
    }

    /**
     * Проверка на Null
     * Проверка firstname.
     * Проверка lastname.
     * Проверка пароля.
     */
    @Override
    public boolean isValid(UserIncomingDto dto) {

        return dto != null
               && isNotBlank(dto.getFirstName())
               && isNotBlank(dto.getLastName())
               && isNotBlank(dto.getPassword());
    }

    private static boolean isNotBlank(String s) {
        return s != null
               && !s.isEmpty()
               && !s.isBlank();
    }
}
