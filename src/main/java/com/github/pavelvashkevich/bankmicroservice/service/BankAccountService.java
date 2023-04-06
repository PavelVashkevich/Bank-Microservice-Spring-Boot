package com.github.pavelvashkevich.bankmicroservice.service;

import com.github.pavelvashkevich.bankmicroservice.model.BankAccount;

public interface BankAccountService {

    BankAccount findByAccountNumber(int accountNumber);
}
