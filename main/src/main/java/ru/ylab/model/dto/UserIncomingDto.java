package ru.ylab.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.StringJoiner;

/**
 * Входящее DTO используется при создании пользователя.
 * <p>
 * Проверка на Null
 * Проверка firstname.
 * Проверка lastname.
 * Проверка пароля.
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public String toString() {
        return new StringJoiner(", ", UserIncomingDto.class.getSimpleName() + "[", "]")
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("password='*****'")
                .toString();
    }
}
