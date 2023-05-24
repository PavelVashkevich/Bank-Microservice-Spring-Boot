package com.github.pavelvashkevich.bankmicroservice.service;

import com.github.pavelvashkevich.bankmicroservice.model.cassandra.CurrencyExchange;
import com.github.pavelvashkevich.bankmicroservice.model.cassandra.CurrencyExchangeKey;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Exchange;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public interface CurrencyExchangeService {

    CurrencyExchange findById(CurrencyExchangeKey currencyExchangeKey);

    void saveAll(List<CurrencyExchange> currencyExchangesForNewDay);

    void updateAll(List<CurrencyExchange> updatedCurrencyExchangesWithCloseRate);

    CurrencyExchangeKey buildCurrencyExchangeKey(Exchange exchange, LocalDate date);

    BigDecimal getCurrentExchangeRate(Exchange exchange);
}