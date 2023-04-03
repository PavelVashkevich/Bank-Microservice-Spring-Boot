package com.github.pavelvashkevich.bankmicroservice.service.impl;

import com.github.pavelvashkevich.bankmicroservice.model.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.model.Client;
import com.github.pavelvashkevich.bankmicroservice.repository.LimitRepository;
import com.github.pavelvashkevich.bankmicroservice.service.AccountLimitService;
import com.github.pavelvashkevich.bankmicroservice.types.ExpenseCategory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AccountLimitServiceIml implements AccountLimitService {

    private final LimitRepository limitRepository;

    @Override
    @Transactional
    public void save(AccountLimit limit) {
        limitRepository.save(limit);
    }

    @Override
    @Transactional
    public void update(AccountLimit limit) {
        //TODO
    }

    @Override
    public Optional<AccountLimit> findLastByExpenseCategoryAndClient(ExpenseCategory expenseCategory, Client client) {
        // TODO
        return Optional.empty();
    }

    @Override
    public List<AccountLimit> findByYearMonthDay(String date) {
        return limitRepository.findByYearMonthDay(date);
    }
}
