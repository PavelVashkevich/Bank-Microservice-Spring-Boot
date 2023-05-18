package com.github.pavelvashkevich.bankmicroservice.util;

import com.github.pavelvashkevich.bankmicroservice.model.postgres.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.service.AccountLimitService;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MonthlyAccountLimitUpdater {
    private final static long NUM_OF_MONTH_TO_SUBST = 1L;
    private final AccountLimitService accountLimitService;

    @Value("${limit.defaultCurrency}")
    private Currency limitCurrency;
    @Value("${limitUpdater.batchSize}")
    private Integer batchSize;

    // TODO Wrong implementation
    // If we take into account that client can set many limits every day method will return all limits set by client

    @Scheduled(cron = "${limitUpdater.defaultLimit.cronExpression}")
    public void addDefaultLimitForNewMonth() {
        List<AccountLimit> limitsSetMonthBefore = getLimitsSetMonthBefore();
        List<AccountLimit> newMonthDefaultAccountLimits = createNewMonthDefaultAccountLimits(limitsSetMonthBefore);
        saveNewDefaultLimitsInBulk(newMonthDefaultAccountLimits);
    }

    private List<AccountLimit> getLimitsSetMonthBefore() {
        LocalDate monthBefore = LocalDate.now().minusMonths(NUM_OF_MONTH_TO_SUBST);
        return accountLimitService.findByYearMonthDay(monthBefore);
    }

    private List<AccountLimit> createNewMonthDefaultAccountLimits(List<AccountLimit> limitsSetMonthBefore) {
        List<AccountLimit> newMonthDefaultAccountLimits = new ArrayList<>();
        for (AccountLimit limitSetMonthBefore: limitsSetMonthBefore) {
            AccountLimit newMonthDefaultAccountLimit = createNewMonthDefaultAccountLimit(limitSetMonthBefore);
            newMonthDefaultAccountLimits.add(newMonthDefaultAccountLimit);
        }
        return newMonthDefaultAccountLimits;
    }

    private AccountLimit createNewMonthDefaultAccountLimit(AccountLimit limitSetMonthBefore) {
        return AccountLimit.builder()
                .currencyShortname(limitCurrency)
                .expenseCategory(limitSetMonthBefore.getExpenseCategory())
                .sum(BigDecimal.ZERO)
                .remainingSum(BigDecimal.ZERO)
                .datetime(ZonedDateTime.now())
                .bankAccount(limitSetMonthBefore.getBankAccount()).build();
    }

    private void saveNewDefaultLimitsInBulk(List<AccountLimit> newLimitsToSet) {
        for(int i = 0; i < batchSize; i += batchSize) {
            List<AccountLimit> batch =
                    newLimitsToSet.subList(i, Math.min(i + batchSize, newLimitsToSet.size()));
            accountLimitService.saveAll(batch);
            accountLimitService.flush();
        }
    }
}
