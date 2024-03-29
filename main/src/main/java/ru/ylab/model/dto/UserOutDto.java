package ru.ylab.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Исходящее DTO используется при создании пользователя.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserOutDto {
    private Long id;
    private String firstName;
    private String lastName;

}
