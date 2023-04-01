package com.github.pavelvashkevich.bankmicroservice.service.impl;

import com.github.pavelvashkevich.bankmicroservice.exception.NoDataFoundException;
import com.github.pavelvashkevich.bankmicroservice.model.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.repository.BankAccountRepository;
import com.github.pavelvashkevich.bankmicroservice.service.BankAccountService;
import com.github.pavelvashkevich.bankmicroservice.util.MessageResourceBundler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Override
    public BankAccount findByAccountNumber(int accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new NoDataFoundException(String.format(MessageResourceBundler.NO_ACCOUNT_NUM_MSG, accountNumber)));
    }
}
