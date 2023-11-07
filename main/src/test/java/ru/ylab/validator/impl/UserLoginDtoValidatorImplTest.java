package ru.ylab.validator.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.ylab.model.dto.UserLoginDto;

class UserLoginDtoValidatorImplTest {

    @DisplayName("UserLoginDto - validation")
    @ParameterizedTest
    @CsvSource({
            "1, 'password', true",
            "0, 'password', false",
            "0, '', false"
    })
    void isValid(Long id, String password, Boolean exceptedValue) {
        UserLoginDtoValidatorImpl userLoginDtoValidator = new UserLoginDtoValidatorImpl();

        UserLoginDto dto = new UserLoginDto(id, password);

        Assertions.assertEquals(exceptedValue, userLoginDtoValidator.isValid(dto, null));
    }
}