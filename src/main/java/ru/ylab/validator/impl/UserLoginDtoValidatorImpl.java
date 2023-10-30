package ru.ylab.validator.impl;

import org.springframework.stereotype.Component;
import ru.ylab.model.dto.UserLoginDto;
import ru.ylab.validator.UserLoginDtoValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Проверка на Null
 * Проверка Входящего числа.
 * Проверка пароля.
 */
@Component
public class UserLoginDtoValidatorImpl implements ConstraintValidator<UserLoginDtoValidation, UserLoginDto> {
    private static boolean isNotBlank(String s) {
        return s != null
               && !s.isEmpty()
               && !s.isBlank();
    }

    @Override
    public boolean isValid(UserLoginDto dto, ConstraintValidatorContext constraintValidatorContext) {
        return dto != null
               && dto.getUserId() != null
               && dto.getUserId() > 0
               && isNotBlank(dto.getPassword());
    }
}
