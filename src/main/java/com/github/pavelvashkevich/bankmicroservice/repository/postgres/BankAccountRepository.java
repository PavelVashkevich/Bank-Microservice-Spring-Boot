package com.github.pavelvashkevich.bankmicroservice.repository.postgres;

import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Bank Account Repository interface defines operations to access and manipulate Account Limits.
 *
 * @author paulvashkevich@gmail.com
 *
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByAccountNumber(long accountNumber);
}