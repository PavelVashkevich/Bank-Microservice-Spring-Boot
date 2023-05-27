package com.github.pavelvashkevich.bankmicroservice.service.impl;

import com.github.pavelvashkevich.bankmicroservice.model.cassandra.CurrencyExchange;
import com.github.pavelvashkevich.bankmicroservice.model.cassandra.CurrencyExchangeKey;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Exchange;
import com.github.pavelvashkevich.bankmicroservice.repository.cassandra.CurrencyExchangeRepository;
import com.github.pavelvashkevich.bankmicroservice.exception.NoDataFoundException;
import com.github.pavelvashkevich.bankmicroservice.service.CurrencyExchangeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
    private static final String NO_CURRENCY_EXCH_ON_DATE_MSG = "Currency exchange of %s on %s doesn't exist";

    private final CurrencyExchangeRepository currencyExchangeRepository;

    public CurrencyExchange findById(CurrencyExchangeKey currencyExchangeKey) {
        return currencyExchangeRepository.findById(currencyExchangeKey)
                .orElseThrow(() -> new NoDataFoundException(
                        String.format(NO_CURRENCY_EXCH_ON_DATE_MSG, currencyExchangeKey, currencyExchangeKey.getExchangeDate())));
    }

    @Override
    public void saveAll(List<CurrencyExchange> currencyExchangesForNewDay) {
        currencyExchangeRepository.saveAll(currencyExchangesForNewDay);
    }

    @Override
    public void updateAll(List<CurrencyExchange> updatedCurrencyExchangesWithCloseRate) {
        currencyExchangeRepository.saveAll(updatedCurrencyExchangesWithCloseRate);
    }

    @Override
    public CurrencyExchangeKey buildCurrencyExchangeKey(Exchange exchange, LocalDate date) {
        return CurrencyExchangeKey.builder().symbol(exchange.getSymbol()).exchangeDate(date).build();
    }

    @Override
    public BigDecimal getCurrentExchangeRate(Exchange exchange) {
        CurrencyExchangeKey exchangeKey = buildCurrencyExchangeKey(exchange, LocalDate.now());
        CurrencyExchange currencyExchange = findById(exchangeKey);
        return currencyExchange.getRate();
    }
}