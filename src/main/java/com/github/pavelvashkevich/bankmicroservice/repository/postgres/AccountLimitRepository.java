package com.github.pavelvashkevich.bankmicroservice.repository.postgres;

import com.github.pavelvashkevich.bankmicroservice.model.postgres.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Account Limit Repository interface defines operations to access and manipulate Account Limits.
 *
 * @author paulvashkevich@gmail.com
 *
 */
@Repository
public interface AccountLimitRepository extends JpaRepository<AccountLimit, Long> {

    Optional<AccountLimit> findByExpenseCategoryAndBankAccount(ExpenseCategory expenseCategory, BankAccount bankAccount);

    @Query(value = "SELECT * FROM account_limit WHERE date_trunc('DAY', datetime) = ?1", nativeQuery = true)
    List<AccountLimit> findByYearMonthDay(LocalDate date);
}
