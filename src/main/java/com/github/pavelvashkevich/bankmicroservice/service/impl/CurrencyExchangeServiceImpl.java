package com.github.pavelvashkevich.bankmicroservice.service.impl;

import com.github.pavelvashkevich.bankmicroservice.exception.NoDataFoundException;
import com.github.pavelvashkevich.bankmicroservice.model.CurrencyExchange;
import com.github.pavelvashkevich.bankmicroservice.repository.CurrencyExchangeRepository;
import com.github.pavelvashkevich.bankmicroservice.service.CurrencyExchangeService;
import com.github.pavelvashkevich.bankmicroservice.types.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private final CurrencyExchangeRepository currencyExchangeRepository;

    @Autowired
    public CurrencyExchangeServiceImpl(CurrencyExchangeRepository currencyExchangeRepository) {
        this.currencyExchangeRepository = currencyExchangeRepository;
    }

    @Override
    @Transactional
    public void save(CurrencyExchange currencyExchange) {
        enrichCurrencyExchange(currencyExchange);
        currencyExchangeRepository.save(currencyExchange);
    }

    private void enrichCurrencyExchange(CurrencyExchange currencyExchange) {
        currencyExchange.setDatetime(LocalDate.now());
    }

    @Override
    @Transactional
    public void update(CurrencyExchange updatedCurrencyExchange) {
        currencyExchangeRepository.save(updatedCurrencyExchange);
    }

    @Override
    public CurrencyExchange findBySymbolAndDateTime(Exchange exchange, LocalDate dateTime) {
        return currencyExchangeRepository.findBySymbolAndDatetime(exchange.getSymbol(), dateTime)
                .orElseThrow(() -> new NoDataFoundException(
                        String.format("Currency exchange of %s on %s not found", exchange.getSymbol(), dateTime)));
    }

    @Override
    public CurrencyExchange findLatestBySymbol(Exchange exchange) {
        return currencyExchangeRepository.findLatestBySymbol(exchange.getSymbol())
                .orElseThrow(() -> new NoDataFoundException(
                        String.format("Latest currency exchange of %s not found", exchange.getSymbol())));
    }
}