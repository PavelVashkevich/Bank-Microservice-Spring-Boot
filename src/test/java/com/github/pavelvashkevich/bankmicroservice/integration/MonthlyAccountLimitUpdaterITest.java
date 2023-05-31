package com.github.pavelvashkevich.bankmicroservice.integration;

import com.github.pavelvashkevich.bankmicroservice.model.postgres.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.Client;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Currency;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.ExpenseCategory;
import com.github.pavelvashkevich.bankmicroservice.repository.postgres.AccountLimitRepository;
import com.github.pavelvashkevich.bankmicroservice.repository.postgres.ClientRepository;
import com.github.pavelvashkevich.bankmicroservice.util.MonthlyAccountLimitUpdater;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class MonthlyAccountLimitUpdaterITest extends AbstractBaseITest {
    private final static int LIMITS_COUNT = 9;

    @Autowired
    private MonthlyAccountLimitUpdater monthlyAccountLimitUpdater;
    @Autowired
    private AccountLimitRepository accountLimitRepository;
    @Autowired
    private ClientRepository clientRepository;


    @BeforeEach
    public void savePreviousMonthLimits() {
        List<AccountLimit> limitsToSave = new ArrayList<>();
        for (int i = 1; i < 1 + LIMITS_COUNT; i++) {
            BankAccount bankAccount = new BankAccount();
            bankAccount.setAccountNumber(Long.parseLong(String.format("%d111111111", i)));
            Client client = Client.builder()
                    .bankAccount(bankAccount)
                    .registrationDate(LocalDate.now().minusMonths(1))
                    .build();
            bankAccount.setClient(client);
            clientRepository.save(client);

            AccountLimit accountLimit = AccountLimit.builder()
                    .bankAccount(bankAccount)
                    .remainingSum(BigDecimal.TEN)
                    .sum(BigDecimal.TEN)
                    .expenseCategory(ExpenseCategory.PRODUCT)
                    .currencyShortname(Currency.USD)
                    .datetime(ZonedDateTime.now().minusMonths(1))
                    .build();
            limitsToSave.add(accountLimit);
        }
        accountLimitRepository.saveAll(limitsToSave);
    }

    @AfterEach
    public void cleanResources() {
        clientRepository.deleteAll();
        accountLimitRepository.deleteAll();
    }

    @Test
    public void givenLimitSetMonthBefore_whenAddDefaultLimits_thenVerifyTheResult() {
        monthlyAccountLimitUpdater.addDefaultLimitForNewMonth();

        List<AccountLimit> defaultLimits = accountLimitRepository.findByDate(LocalDate.now());

        Assertions.assertThat(defaultLimits).isNotNull();
        Assertions.assertThat(defaultLimits.size()).isEqualTo(LIMITS_COUNT);
        defaultLimits.forEach(defaultLimit -> {
            Assertions.assertThat(defaultLimit.getId()).isNotNull();
            Assertions.assertThat(defaultLimit.getSum()).isEqualTo(BigDecimal.ZERO);
            Assertions.assertThat(defaultLimit.getRemainingSum()).isEqualTo(BigDecimal.ZERO);
            Assertions.assertThat(defaultLimit.getCurrencyShortname()).isEqualTo(Currency.USD);
            Assertions.assertThat(defaultLimit.getDatetime().toLocalDate()).isEqualTo(LocalDate.now());
            Assertions.assertThat(defaultLimit.getExpenseCategory()).isEqualTo(ExpenseCategory.PRODUCT);
        });
    }

}