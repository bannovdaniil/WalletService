package ru.ylab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Входящее DTO используется при создании пользователя.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserIncomingDto {
    private String firstName;
    private String lastName;
    private String password;
}
