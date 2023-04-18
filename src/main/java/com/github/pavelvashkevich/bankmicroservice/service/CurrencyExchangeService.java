package com.github.pavelvashkevich.bankmicroservice.service;

import com.github.pavelvashkevich.bankmicroservice.model.CurrencyExchange;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Exchange;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyExchangeService {

    void save(CurrencyExchange currencyExchange);

    void update(CurrencyExchange updatedCurrencyExchange);

    CurrencyExchange findBySymbolAndExchangeDate(Exchange exchange, LocalDate exchangeDate);

    CurrencyExchange findLatestBySymbol(Exchange exchange);

    void saveAll(List<CurrencyExchange> currencyExchangesForNewDay);

    void updateAll(List<CurrencyExchange> updatedCurrencyExchangesWithCloseRate);
}