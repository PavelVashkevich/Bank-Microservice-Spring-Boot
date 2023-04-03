package com.github.pavelvashkevich.bankmicroservice.util;

import com.github.pavelvashkevich.bankmicroservice.model.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.service.AccountLimitService;
import com.github.pavelvashkevich.bankmicroservice.types.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MonthlyAccountLimitUpdater {

    private final static long NUM_OF_MONTH_TO_SUBST = 1L;
    private final AccountLimitService accountLimitService;
    @Value("${limit.default_currency}")
    private Currency limitCurrency;

    @Scheduled(cron = "${limitupdater.default_limit.cron_expression}")
    public void addDefaultLimitForNewMonth() {
        LocalDate monthBefore = LocalDate.now().minusMonths(NUM_OF_MONTH_TO_SUBST);
        List<AccountLimit> limitsSetMonthBefore
                = accountLimitService.findByYearMonthDay(monthBefore.toString());
        for (AccountLimit limitSetMonthBefore: limitsSetMonthBefore) {
            AccountLimit newLimit = new AccountLimit();
            newLimit.setCurrencyShortname(limitCurrency);
            newLimit.setExpenseCategory(limitSetMonthBefore.getExpenseCategory());
            newLimit.setSum(BigDecimal.ZERO);
            newLimit.setDatetime(ZonedDateTime.now());
            newLimit.setBankAccount(limitSetMonthBefore.getBankAccount());
            accountLimitService.save(newLimit);
        }
    }
}
