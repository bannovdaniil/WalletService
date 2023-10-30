package ru.ylab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.ylab.validator.UserLoginDtoValidation;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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
