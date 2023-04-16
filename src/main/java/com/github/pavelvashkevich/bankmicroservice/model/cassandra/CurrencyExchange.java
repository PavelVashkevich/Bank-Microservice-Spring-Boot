package com.github.pavelvashkevich.bankmicroservice.model.cassandra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table("currency_exchange")
public class CurrencyExchange {
    @PrimaryKey
    private CurrencyExchangeKey currencyExchangeKey;
    @PositiveOrZero(message = "Rate on close must be positive or zero.")
    @NotNull
    private BigDecimal rate;
}
