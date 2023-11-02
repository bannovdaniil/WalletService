package ru.ylab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
