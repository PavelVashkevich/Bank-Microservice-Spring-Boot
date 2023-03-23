package com.github.pavelvashkevich.bankmicroservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@Table(name = "currency_exchanges")
public class CurrencyExchange {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "symbol")
    @NotBlank(message = "Symbol cannot be null or blank.")
    @Pattern(regexp = "[A-Z]{3}/[A-Z]{3}", message = "Symbol must be in format 'Currency code/Currency code'")
    private String symbol;

    @Column(name = "rate")
    @PositiveOrZero(message = "Rate on close must be positive or zero.")
    @NotNull
    private BigDecimal rate;

    @Column(name = "datetime", columnDefinition = "DATE")
    @PastOrPresent
    @NotNull
    private LocalDate datetime;
}