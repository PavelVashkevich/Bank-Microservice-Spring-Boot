package com.github.pavelvashkevich.bankmicroservice.validator;


import com.github.pavelvashkevich.bankmicroservice.repository.postgres.BankAccountRepository;
import com.github.pavelvashkevich.bankmicroservice.model.types.annotations.UniqueBankAccountNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UniqueBankAccountValidator implements ConstraintValidator<UniqueBankAccountNumber, Long> {
    private final BankAccountRepository bankAccountRepository;

    public UniqueBankAccountValidator(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }
        return !isBankAccountWithNumberExist(value);
    }

    private boolean isBankAccountWithNumberExist(long accountNumber) {
        return (bankAccountRepository.findByAccountNumber(accountNumber).isPresent());
    }
}
