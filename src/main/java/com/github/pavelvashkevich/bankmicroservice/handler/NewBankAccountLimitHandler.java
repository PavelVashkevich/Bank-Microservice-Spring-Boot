package com.github.pavelvashkevich.bankmicroservice.handler;

import com.github.pavelvashkevich.bankmicroservice.model.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.model.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.service.impl.AccountLimitServiceIml;
import com.github.pavelvashkevich.bankmicroservice.types.Currency;
import com.github.pavelvashkevich.bankmicroservice.types.ExpenseCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.EnumSet;

@Component
@RequiredArgsConstructor
public class NewBankAccountLimitHandler {

    private final AccountLimitServiceIml accountLimitServiceIml;
    @Value("${limit.default_currency}")
    private Currency limitCurrency;
    private final EnumSet<ExpenseCategory> expenseCategories = EnumSet.allOf(ExpenseCategory.class);

    public void addAccountLimitToTheNewBankAccount(BankAccount bankAccount) {
        expenseCategories.forEach(expenseCategory ->
        {
            AccountLimit accountLimit = createAccountLimitForSpecificExpenseCategory(bankAccount, expenseCategory);
            accountLimitServiceIml.save(accountLimit);
        });
    }

    private AccountLimit createAccountLimitForSpecificExpenseCategory(BankAccount bankAccount,
                                                                      ExpenseCategory expenseCategory) {
        AccountLimit accountLimit = new AccountLimit();
        accountLimit.setCurrencyShortname(limitCurrency);
        accountLimit.setExpenseCategory(expenseCategory);
        accountLimit.setSum(BigDecimal.ZERO);
        accountLimit.setDatetime(ZonedDateTime.now());
        accountLimit.setBankAccount(bankAccount);
        return accountLimit;
    }
}