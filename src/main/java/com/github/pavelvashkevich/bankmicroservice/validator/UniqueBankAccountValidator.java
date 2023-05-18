package com.github.pavelvashkevich.bankmicroservice.validator;


import com.github.pavelvashkevich.bankmicroservice.model.types.annotations.UniqueBankAccountNumber;
import com.github.pavelvashkevich.bankmicroservice.service.BankAccountService;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@AllArgsConstructor
public class UniqueBankAccountValidator implements ConstraintValidator<UniqueBankAccountNumber, Long> {
    private final BankAccountService bankAccountService;

    private boolean value;

    @Override
    public void initialize(UniqueBankAccountNumber annotation) {
        value = annotation.value();
    }

    @Override
    public boolean isValid(Long accountNumber, ConstraintValidatorContext context) {
        if (Objects.isNull(accountNumber)) {
            return true;
        }
        return value != bankAccountService.isBankAccountWithNumberExist(accountNumber);
    }
}
