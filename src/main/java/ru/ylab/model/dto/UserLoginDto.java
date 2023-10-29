package ru.ylab.model.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.ylab.validator.UserLoginDtoValidation;

/**
 * Для передачи данных при логине.
 */
@UserLoginDtoValidation
@Getter
@AllArgsConstructor
public class UserLoginDto {
    @Positive
    private Long userId;
    @Size(min = 1)
    private String password;
}
