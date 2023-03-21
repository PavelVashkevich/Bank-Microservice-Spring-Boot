package com.github.pavelvashkevich.bankmicroservice.service;

import com.github.pavelvashkevich.bankmicroservice.model.CurrencyExchange;
import com.github.pavelvashkevich.bankmicroservice.types.Exchange;

import java.time.LocalDate;

public interface CurrencyExchangeService {

    void save(CurrencyExchange currencyExchange);

    void update(CurrencyExchange updatedCurrencyExchange);

    CurrencyExchange findBySymbolAndDateTime(Exchange exchange, LocalDate dateTime);

    CurrencyExchange findLatestBySymbol(Exchange exchange);
}