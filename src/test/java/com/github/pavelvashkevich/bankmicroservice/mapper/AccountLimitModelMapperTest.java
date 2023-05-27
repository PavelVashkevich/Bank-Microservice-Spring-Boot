package com.github.pavelvashkevich.bankmicroservice.mapper;

import com.github.pavelvashkevich.bankmicroservice.config.ModelMapperConfig;
import com.github.pavelvashkevich.bankmicroservice.dto.limit.AccountLimitAddResponseDto;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Currency;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.ExpenseCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ModelMapperConfig.class, AccountLimitModelMapper.class})
class AccountLimitModelMapperTest {

    @Autowired
    private AccountLimitModelMapper accountLimitModelMapper;

    @DisplayName("JUnit test for the 'mapAccountLimitToAddResponseDto' method of the AccountLimitModelMapper class")
    @Test
    public void givenAccountLimit_whenMapToAccountLimitAddResponseDto_thenVerifyResult() {
        AccountLimit accountLimit = AccountLimit.builder()
                .id(1L)
                .currencyShortname(Currency.USD)
                .expenseCategory(ExpenseCategory.PRODUCT)
                .sum(BigDecimal.TEN)
                .remainingSum(BigDecimal.TEN)
                .datetime(ZonedDateTime.now())
                .build();

        AccountLimitAddResponseDto mappedAddResponseDto = accountLimitModelMapper.mapAccountLimitToAddResponseDto(accountLimit);

        assertThat(mappedAddResponseDto).isNotNull();
        assertThat(mappedAddResponseDto.getId()).isEqualTo(accountLimit.getId());
        assertThat(mappedAddResponseDto.getLimitSum()).isEqualTo(accountLimit.getSum());
        assertThat(mappedAddResponseDto.getLimitDatetime()).isEqualTo(accountLimit.getDatetime());
        assertThat(mappedAddResponseDto.getLimitCurrencyShortname()).isEqualTo(accountLimit.getCurrencyShortname());
    }

}