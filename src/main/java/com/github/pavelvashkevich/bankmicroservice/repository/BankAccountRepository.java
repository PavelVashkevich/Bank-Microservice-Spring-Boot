package com.github.pavelvashkevich.bankmicroservice.repository;

import com.github.pavelvashkevich.bankmicroservice.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByAccountNumber(int accountNumber);
}