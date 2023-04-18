package com.github.pavelvashkevich.bankmicroservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class CurrencyExchange {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator_currency_exchange")
    @SequenceGenerator(name = "seq_generator_currency_exchange", sequenceName = "currency_exchange_id_seq",
            allocationSize = 1)
    private Long id;
    @NotBlank(message = "Symbol cannot be null or blank.")
    @Pattern(regexp = "[A-Z]{3}/[A-Z]{3}", message = "Symbol must be in format 'Currency code/Currency code'")
    private String symbol;
    @PositiveOrZero(message = "Rate on close must be positive or zero.")
    @NotNull
    private BigDecimal rate;
    @PastOrPresent
    @NotNull
    private LocalDate exchangeDate;
}