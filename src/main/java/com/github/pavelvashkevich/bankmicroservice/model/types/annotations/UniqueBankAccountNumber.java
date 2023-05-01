package com.github.pavelvashkevich.bankmicroservice.model.types.annotations;

import com.github.pavelvashkevich.bankmicroservice.validator.UniqueBankAccountValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UniqueBankAccountValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueBankAccountNumber {
    String message() default "The given bank account number is already in use";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
