package ru.ylab.model.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Входящее DTO используется при создании пользователя.
 */
@NoArgsConstructor
@AllArgsConstructor
public class UserOutDto {
    private String firstName;
    private String lastName;
}
