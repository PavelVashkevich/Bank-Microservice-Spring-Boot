package com.github.pavelvashkevich.bankmicroservice.service.impl;

import com.github.pavelvashkevich.bankmicroservice.exception.NoDataFoundException;
import com.github.pavelvashkevich.bankmicroservice.model.CurrencyExchange;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Exchange;
import com.github.pavelvashkevich.bankmicroservice.repository.CurrencyExchangeRepository;
import com.github.pavelvashkevich.bankmicroservice.service.CurrencyExchangeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
    private static final String NO_LATEST_CURRENCY_EXCH_MSG = "Latest currency exchange of %s doesn't exist";
    private static final String NO_CURRENCY_EXCH_ON_DATE_MSG = "Currency exchange of %s on %s doesn't exist";

    private final CurrencyExchangeRepository currencyExchangeRepository;

    @Override
    @Transactional
    public void save(CurrencyExchange currencyExchange) {
        enrichCurrencyExchange(currencyExchange);
        currencyExchangeRepository.save(currencyExchange);
    }

    @Override
    @Transactional
    public void update(CurrencyExchange updatedCurrencyExchange) {
        currencyExchangeRepository.save(updatedCurrencyExchange);
    }

    @Override
    public CurrencyExchange findBySymbolAndExchangeDate(Exchange exchange, LocalDate exchangeDate) {
        return currencyExchangeRepository.findBySymbolAndExchangeDate(exchange.getSymbol(), exchangeDate)
                .orElseThrow(() -> new NoDataFoundException(
                        String.format(NO_CURRENCY_EXCH_ON_DATE_MSG, exchange.getSymbol(), exchangeDate)));
    }

    @Override
    public CurrencyExchange findLatestBySymbol(Exchange exchange) {
        return currencyExchangeRepository.findLatestBySymbol(exchange.getSymbol())
                .orElseThrow(() -> new NoDataFoundException(
                        String.format(NO_LATEST_CURRENCY_EXCH_MSG, exchange.getSymbol())));
    }

    @Override
    @Transactional
    public void saveAll(List<CurrencyExchange> currencyExchangesForNewDay) {
        currencyExchangeRepository.saveAll(currencyExchangesForNewDay);
    }

    @Override
    @Transactional
    public void updateAll(List<CurrencyExchange> updatedCurrencyExchangesWithCloseRate) {
        currencyExchangeRepository.saveAll(updatedCurrencyExchangesWithCloseRate);
    }

    private void enrichCurrencyExchange(CurrencyExchange currencyExchange) {
        currencyExchange.setExchangeDate(LocalDate.now());
    }
}