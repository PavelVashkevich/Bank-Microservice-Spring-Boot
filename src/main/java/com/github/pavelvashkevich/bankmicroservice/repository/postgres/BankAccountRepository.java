package com.github.pavelvashkevich.bankmicroservice.repository.postgres;

import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByAccountNumber(long accountNumber);
}