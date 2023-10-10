package ru.ylab.model.dto;

/**
 * Входящее DTO используется при создании пользователя.
 */
public class UserIncomingDto {
    private String firstName;
    private String lastName;
    private char[] password;

    public UserIncomingDto() {
    }

    public UserIncomingDto(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password.toCharArray();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return String.valueOf(password);
    }

    public void setPassword(String password) {
        this.password = password.toCharArray();
    }
}
