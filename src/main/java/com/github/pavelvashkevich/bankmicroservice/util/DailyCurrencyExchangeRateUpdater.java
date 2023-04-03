package com.github.pavelvashkevich.bankmicroservice.util;

import com.github.pavelvashkevich.bankmicroservice.model.CurrencyExchange;
import com.github.pavelvashkevich.bankmicroservice.service.CurrencyExchangeService;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DailyCurrencyExchangeRateUpdater {

    private final CurrencyExchangeService currencyExchangeService;
    private final TwelveApiUtil twelveApiUtil;

    @Scheduled(cron = "${rateUpdater.rate.previous.cronExpression}")
    public void addCurrencyExchangeRateForNewDay() {
        List<CurrencyExchange> currencyExchangesForNewDay = Stream.of(Exchange.values())
                .map(this::createCurrencyExchangeWithRateOnPreviousClose).collect(Collectors.toList());
        currencyExchangeService.saveAll(currencyExchangesForNewDay);
    }

    @Scheduled(cron = "${rateUpdater.rate.close.cronExpression}", zone = "${rateUpdater.rate.close.timezone}")
    public void updateCurrencyExchangeRateValueForNewDay() {
        List<CurrencyExchange> updatedCurrencyExchangesWithCloseRate = Stream.of(Exchange.values())
                .map(exchange -> {
                    CurrencyExchange currencyExchangeToUpdate = currencyExchangeService.findLatestBySymbol(exchange);
                    Optional<BigDecimal> rateOnClose = twelveApiUtil.getRateOnCloseForSpecificExchange(exchange);
                    rateOnClose.ifPresent(currencyExchangeToUpdate::setRate);
                    return currencyExchangeToUpdate;
                }).collect(Collectors.toList());
        currencyExchangeService.updateAll(updatedCurrencyExchangesWithCloseRate);
    }

    private CurrencyExchange createCurrencyExchangeWithRateOnPreviousClose(Exchange exchange) {
        CurrencyExchange previousDayCurrencyExchange = getPreviousDayCurrencyExchange(exchange);
        CurrencyExchange newDayCurrencyExchange = new CurrencyExchange();
        newDayCurrencyExchange.setSymbol(exchange.getSymbol());
        newDayCurrencyExchange.setRate(previousDayCurrencyExchange.getRate());
        return newDayCurrencyExchange;
    }

    private CurrencyExchange getPreviousDayCurrencyExchange(Exchange exchange) {
        return currencyExchangeService.findLatestBySymbol(exchange);
    }
}