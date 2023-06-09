package com.github.pavelvashkevich.bankmicroservice.service.impl;

import com.github.pavelvashkevich.bankmicroservice.exception.NoDataFoundException;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.repository.postgres.BankAccountRepository;
import com.github.pavelvashkevich.bankmicroservice.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class BankAccountServiceImpl implements BankAccountService {
    private static final String NO_ACCOUNT_NUM_MSG = "Account number %d doesn't exist";

    private final BankAccountRepository bankAccountRepository;

    @Override
    public BankAccount findByAccountNumber(int accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new NoDataFoundException(String.format(NO_ACCOUNT_NUM_MSG, accountNumber)));
    }

    @Override
    public boolean isBankAccountWithNumberExist(long accountNumber) {
        return (bankAccountRepository.findByAccountNumber(accountNumber).isPresent());
    }
}
