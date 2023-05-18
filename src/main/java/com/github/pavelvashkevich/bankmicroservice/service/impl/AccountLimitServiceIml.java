package com.github.pavelvashkevich.bankmicroservice.service.impl;

import com.github.pavelvashkevich.bankmicroservice.dto.limit.AccountLimitAddRequestDto;
import com.github.pavelvashkevich.bankmicroservice.dto.limit.AccountLimitAddResponseDto;
import com.github.pavelvashkevich.bankmicroservice.mapper.AccountLimitModelMapper;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.Client;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Currency;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.ExpenseCategory;
import com.github.pavelvashkevich.bankmicroservice.repository.postgres.AccountLimitRepository;
import com.github.pavelvashkevich.bankmicroservice.service.AccountLimitService;
import com.github.pavelvashkevich.bankmicroservice.service.BankAccountService;
import com.github.pavelvashkevich.bankmicroservice.util.CurrencyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountLimitServiceIml implements AccountLimitService {
    private final AccountLimitRepository accountLimitRepository;
    private final BankAccountService bankAccountService;
    private final CurrencyConverter currencyConverter;
    private final AccountLimitModelMapper accountLimitModelMapper;

    @Value("${limit.defaultCurrency}")
    private Currency defaultLimitCurrency;


    @Override
    @Transactional
    public AccountLimitAddResponseDto save(AccountLimitAddRequestDto accountLimitToAdd) {
        BankAccount bankAccount = bankAccountService.findByAccountNumber(accountLimitToAdd.getAccountNumber());
        BigDecimal limitSum = accountLimitToAdd.getLimitSum();
        Currency currency = Currency.valueOf(accountLimitToAdd.getLimitCurrencyShortname());
        if (currency != Currency.USD) {
            limitSum = currencyConverter.covertSumToUsd(currency, limitSum);
        }
        AccountLimit accountLimit = AccountLimit.builder()
                .currencyShortname(defaultLimitCurrency)
                .expenseCategory(ExpenseCategory.valueOf(accountLimitToAdd.getLimitExpenseCategory()))
                .sum(limitSum)
                .remainingSum(limitSum)
                .datetime(ZonedDateTime.now())
                .bankAccount(bankAccount)
                .build();
        bankAccount.getAccountLimits().add(accountLimit);
        accountLimitRepository.save(accountLimit);
        return accountLimitModelMapper.mapAccountLimitToAddResponseDto(accountLimit);
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
