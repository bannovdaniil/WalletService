package ru.ylab.model.dto;

/**
 * Входящее DTO используется при создании пользователя.
 */

public class UserIncomingDto {
    private String firstName;
    private String lastName;
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
