package ru.ylab.model.dto;


/**
 * Входящее DTO используется при создании пользователя.
 */
public class UserOutDto {
    private Long id;
    private String firstName;
    private String lastName;

    public UserOutDto() {
    }

    public UserOutDto(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
