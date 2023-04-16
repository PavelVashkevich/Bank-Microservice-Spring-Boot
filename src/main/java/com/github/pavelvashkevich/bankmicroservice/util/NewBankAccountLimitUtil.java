package com.github.pavelvashkevich.bankmicroservice.util;

import com.github.pavelvashkevich.bankmicroservice.model.postgres.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.service.impl.AccountLimitServiceIml;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Currency;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.ExpenseCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class NewBankAccountLimitUtil {
    private final AccountLimitServiceIml accountLimitServiceIml;

    @Value("${limit.defaultCurrency}")
    private Currency limitCurrency;

    public void addAccountLimitToTheNewBankAccount(BankAccount bankAccount) {
        List<AccountLimit> accountLimitForExpenseCategories = Stream.of(ExpenseCategory.values()).map(expenseCategory ->
                createAccountLimitForSpecificExpenseCategory(bankAccount, expenseCategory)).collect(Collectors.toList());
        accountLimitServiceIml.saveAll(accountLimitForExpenseCategories);
    }

    private AccountLimit createAccountLimitForSpecificExpenseCategory(BankAccount bankAccount,
                                                                      ExpenseCategory expenseCategory) {
        return AccountLimit.builder()
                .currencyShortname(limitCurrency)
                .expenseCategory(expenseCategory)
                .sum(BigDecimal.ZERO)
                .remainingSum(BigDecimal.ZERO)
                .datetime(ZonedDateTime.now())
                .bankAccount(bankAccount).build();
    }
}