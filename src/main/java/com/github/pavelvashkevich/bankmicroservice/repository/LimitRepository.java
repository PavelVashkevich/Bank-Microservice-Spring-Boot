package com.github.pavelvashkevich.bankmicroservice.repository;

import com.github.pavelvashkevich.bankmicroservice.model.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.model.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.types.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LimitRepository extends JpaRepository<AccountLimit, Long> {

    Optional<AccountLimit> findByExpenseCategoryAndBankAccount(ExpenseCategory expenseCategory, BankAccount bankAccount);

    @Query("FROM AccountLimit a WHERE date_trunc('DAY', a.datetime) = TO_DATE(:date, 'yyyy-mm-dd')")
    List<AccountLimit> findByYearMonthDay(@Param("date") String date);
}
