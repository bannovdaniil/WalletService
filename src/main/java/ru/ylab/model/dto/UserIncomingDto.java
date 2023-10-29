package ru.ylab.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Входящее DTO используется при создании пользователя.
 * <p>
 * Проверка на Null
 * Проверка firstname.
 * Проверка lastname.
 * Проверка пароля.
 */

public class UserIncomingDto {
    @NotNull
    @NotBlank
    @Size(min = 1)
    private String firstName;
    @NotNull
    @NotBlank
    @Size(min = 1)
    private String lastName;
    @NotNull
    @NotBlank
    @Size(min = 1)
    private String password;

    public UserIncomingDto() {
    }

    public UserIncomingDto(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
}
