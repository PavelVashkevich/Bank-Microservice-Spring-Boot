package com.github.pavelvashkevich.bankmicroservice.validator;

import com.github.pavelvashkevich.bankmicroservice.model.types.annotations.UniqueBankAccountNumber;
import com.github.pavelvashkevich.bankmicroservice.repository.BankAccountRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UniqueBankAccountValidator implements ConstraintValidator<UniqueBankAccountNumber, Integer> {
    private final BankAccountRepository bankAccountRepository;

    public UniqueBankAccountValidator(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }
        return !isBankAccountWithNumberExist(value);
    }

    private boolean isBankAccountWithNumberExist(int accountNumber) {
        return (bankAccountRepository.findByAccountNumber(accountNumber).isPresent());
    }
}
