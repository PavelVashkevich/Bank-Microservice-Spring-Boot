package com.github.pavelvashkevich.bankmicroservice.dto.limit;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Currency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountLimitAddResponseDto {
    private Long id;
    private BigDecimal limitSum;
    private ZonedDateTime limitDatetime;
    private Currency limitCurrencyShortname;
}
