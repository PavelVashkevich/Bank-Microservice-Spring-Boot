package com.github.pavelvashkevich.bankmicroservice.service;

import com.github.pavelvashkevich.bankmicroservice.model.CurrencyExchange;
import com.github.pavelvashkevich.bankmicroservice.types.Exchange;

import java.time.LocalDate;

public interface CurrencyExchangeService {

    void save(CurrencyExchange currencyExchange);

    void update(CurrencyExchange updatedCurrencyExchange);

    CurrencyExchange findBySymbolAndExchangeDate(Exchange exchange, LocalDate exchangeDate);

    CurrencyExchange findLatestBySymbol(Exchange exchange);
}