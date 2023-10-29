package ru.ylab.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Для передачи данных при логине.
 */
public class UserLoginDto {
    @Positive
    private Long userId;
    @NotNull
    @NotBlank
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
