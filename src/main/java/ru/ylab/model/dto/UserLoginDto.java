package ru.ylab.model.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.ylab.validator.UserLoginDtoValidation;

import java.util.StringJoiner;

/**
 * Для передачи данных при логине.
 */
@UserLoginDtoValidation
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    @Positive
    private Long userId;
    @Size(min = 1)
    private String password;

    @Override
    public String toString() {
        return new StringJoiner(", ", UserLoginDto.class.getSimpleName() + "[", "]")
                .add("userId=" + userId)
                .add("password='*****'")
                .toString();
    }
}
