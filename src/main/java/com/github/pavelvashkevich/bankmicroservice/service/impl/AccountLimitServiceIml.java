package com.github.pavelvashkevich.bankmicroservice.service.impl;

import com.github.pavelvashkevich.bankmicroservice.model.postgres.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.Client;
import com.github.pavelvashkevich.bankmicroservice.repository.postgres.AccountLimitRepository;
import com.github.pavelvashkevich.bankmicroservice.service.AccountLimitService;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.ExpenseCategory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AccountLimitServiceIml implements AccountLimitService {
    private final AccountLimitRepository accountLimitRepository;

    @Override
    @Transactional
    public void save(AccountLimit limit) {
        accountLimitRepository.save(limit);
    }

    @Override
    @Transactional
    public void update(AccountLimit limit) {
        accountLimitRepository.save(limit);
    }

    @Override
    public Optional<AccountLimit> findLastByExpenseCategoryAndClient(ExpenseCategory expenseCategory, Client client) {
        // TODO
        return Optional.empty();
    }

    @Override
    public List<AccountLimit> findByYearMonthDay(LocalDate date) {
        return accountLimitRepository.findByYearMonthDay(date);
    }

    @Override
    @Transactional
    public void saveAll(List<AccountLimit> accountLimitForExpenseCategories) {
        accountLimitRepository.saveAll(accountLimitForExpenseCategories);
    }

    @Override
    @Transactional
    public void flush() {
        accountLimitRepository.flush();
    }
}
