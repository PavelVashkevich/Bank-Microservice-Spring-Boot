package com.github.pavelvashkevich.bankmicroservice.service;

import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;

public interface BankAccountService {

    BankAccount findByAccountNumber(int accountNumber);
}
