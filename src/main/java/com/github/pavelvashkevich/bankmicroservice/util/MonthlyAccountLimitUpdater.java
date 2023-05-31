package com.github.pavelvashkevich.bankmicroservice.util;

import com.github.pavelvashkevich.bankmicroservice.model.postgres.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Currency;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.ExpenseCategory;
import com.github.pavelvashkevich.bankmicroservice.service.AccountLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MonthlyAccountLimitUpdater {
    private final static long NUM_OF_MONTH_TO_SUBST = 1L;
    private final AccountLimitService accountLimitService;

    @Value("${limit.defaultCurrency}")
    private Currency limitCurrency;
    @Value("${limitUpdater.batchSize}")
    private Integer batchSize;

    @Scheduled(cron = "${limitUpdater.defaultLimit.cronExpression}")
    public void addDefaultLimitForNewMonth() {
        List<AccountLimit> limitsSetMonthBefore = getLimitsSetMonthBefore();
        Map<BankAccount, List<AccountLimit>> bankAccountToAccountLimits =
                limitsSetMonthBefore.stream().collect(Collectors.groupingBy(AccountLimit::getBankAccount));
        Map<BankAccount, Set<ExpenseCategory>> bankAccountToExpenseCategoryToUpdate = new HashMap<>();
        bankAccountToAccountLimits.forEach((bankAccount, accountLimits) -> {
            Set<ExpenseCategory> expenseCategories = accountLimits
                    .stream().map(AccountLimit::getExpenseCategory).collect(Collectors.toSet());
            bankAccountToExpenseCategoryToUpdate.put(bankAccount, expenseCategories);
        });
        List<AccountLimit> newMonthDefaultAccountLimits = createNewMonthDefaultAccountLimits(bankAccountToExpenseCategoryToUpdate);
        saveNewDefaultLimitsInBulk(newMonthDefaultAccountLimits);
    }

    private List<AccountLimit> getLimitsSetMonthBefore() {
        LocalDate monthBefore = LocalDate.now().minusMonths(NUM_OF_MONTH_TO_SUBST);
        return accountLimitService.findByDate(monthBefore);
    }

    private List<AccountLimit> createNewMonthDefaultAccountLimits(Map<BankAccount, Set<ExpenseCategory>> bankAccountToExpenseCategoryToUpdate) {
        List<AccountLimit> newMonthDefaultAccountLimits = new ArrayList<>();
        bankAccountToExpenseCategoryToUpdate.forEach((bankAccount, expenseCategories) ->
                expenseCategories.forEach(expenseCategory -> {
                    AccountLimit newDefaultLimit = createNewMonthDefaultAccountLimit(bankAccount,
                            expenseCategory);
                    newMonthDefaultAccountLimits.add(newDefaultLimit);
                }));
        return newMonthDefaultAccountLimits;
    }

    private AccountLimit createNewMonthDefaultAccountLimit(BankAccount bankAccount, ExpenseCategory expenseCategory) {
        return AccountLimit.builder()
                .currencyShortname(limitCurrency)
                .expenseCategory(expenseCategory)
                .sum(BigDecimal.ZERO)
                .remainingSum(BigDecimal.ZERO)
                .datetime(ZonedDateTime.now())
                .bankAccount(bankAccount)
                .build();
    }

    private void saveNewDefaultLimitsInBulk(List<AccountLimit> newLimitsToSet) {
        for (int i = 0; i < batchSize; i += batchSize) {
            List<AccountLimit> batch =
                    newLimitsToSet.subList(i, Math.min(i + batchSize, newLimitsToSet.size()));
            accountLimitService.saveAll(batch);
            accountLimitService.flush();
        }
    }
}
