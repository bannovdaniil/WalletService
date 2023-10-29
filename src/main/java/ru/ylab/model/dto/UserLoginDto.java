package ru.ylab.model.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.ylab.validator.UserLoginDtoValidation;

/**
 * Для передачи данных при логине.
 */
@UserLoginDtoValidation
public class UserLoginDto {
    @Positive
    private Long userId;
    @Size(min = 1)
    private String password;

    public UserLoginDto() {
    }

    public UserLoginDto(Long userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
