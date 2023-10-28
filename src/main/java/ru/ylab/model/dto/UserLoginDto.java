package ru.ylab.model.dto;


public class UserLoginDto {
    private Long userId;
    private String password;

    public UserLoginDto() {
    }

    public UserLoginDto(Long userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
