package com.github.pavelvashkevich.bankmicroservice.service;

import com.github.pavelvashkevich.bankmicroservice.controller.dto.limit.AccountLimitRequestDto;
import com.github.pavelvashkevich.bankmicroservice.model.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.model.Client;
import com.github.pavelvashkevich.bankmicroservice.types.ExpenseCategory;

import java.util.List;
import java.util.Optional;

public interface AccountLimitService {

    void save(AccountLimit limit);

    void save(AccountLimitRequestDto limit);

    void update(AccountLimit limit);

    Optional<AccountLimit> findLastByExpenseCategoryAndClient(ExpenseCategory expenseCategory, Client client);

    List<AccountLimit> findByYearMonthDay(String date);
}
