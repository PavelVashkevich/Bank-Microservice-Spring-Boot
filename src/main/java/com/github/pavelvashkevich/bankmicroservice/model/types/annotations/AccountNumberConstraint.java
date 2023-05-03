package com.github.pavelvashkevich.bankmicroservice.model.types.annotations;

import com.github.pavelvashkevich.bankmicroservice.validator.AccountNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AccountNumberValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountNumberConstraint {
    String message() default "Invalid account number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
