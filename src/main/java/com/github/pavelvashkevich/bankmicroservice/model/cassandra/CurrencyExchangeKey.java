package com.github.pavelvashkevich.bankmicroservice.model.cassandra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.time.LocalDate;

@PrimaryKeyClass
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKeyColumn(name = "symbol",
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED)
    private String symbol;
    @PastOrPresent
    @NotNull
    @PrimaryKeyColumn(name = "exchange_date",
            ordinal = 1, type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING)
    private LocalDate exchangeDate;
}
