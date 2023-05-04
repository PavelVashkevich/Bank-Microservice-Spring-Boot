package com.github.pavelvashkevich.bankmicroservice.validator;

import com.github.pavelvashkevich.bankmicroservice.model.types.annotations.AccountNumberConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class AccountNumberValidator implements ConstraintValidator<AccountNumberConstraint, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (Objects.isNull(value))
            return true;
        return String.valueOf(value).length() == 10;
    }
}
