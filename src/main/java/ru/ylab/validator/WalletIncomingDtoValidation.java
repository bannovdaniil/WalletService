package ru.ylab.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.ylab.validator.impl.WalletIncomingDtoValidatorImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Валидация сущности WalletIncomingDto
 */
@Constraint(validatedBy = WalletIncomingDtoValidatorImpl.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface WalletIncomingDtoValidation {
    String message() default "UserLoginDto fail";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
