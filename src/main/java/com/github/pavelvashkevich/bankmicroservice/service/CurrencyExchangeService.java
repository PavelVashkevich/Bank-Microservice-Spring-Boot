package com.github.pavelvashkevich.bankmicroservice.service;

import com.github.pavelvashkevich.bankmicroservice.model.cassandra.CurrencyExchange;
import com.github.pavelvashkevich.bankmicroservice.model.cassandra.CurrencyExchangeKey;

import java.util.List;


public interface CurrencyExchangeService {

    CurrencyExchange findById(CurrencyExchangeKey currencyExchangeKey);

    void saveAll(List<CurrencyExchange> currencyExchangesForNewDay);

    void updateAll(List<CurrencyExchange> updatedCurrencyExchangesWithCloseRate);
}