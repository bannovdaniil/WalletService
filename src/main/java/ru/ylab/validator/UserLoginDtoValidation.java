package ru.ylab.validator;

import ru.ylab.validator.impl.UserLoginDtoValidatorImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserLoginDtoValidatorImpl.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLoginDtoValidation {
    String message() default "UserLoginDto fail";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
