package ru.ylab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Входящее DTO используется при создании пользователя.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserOutDto {
    private Long id;
    private String firstName;
    private String lastName;
}
