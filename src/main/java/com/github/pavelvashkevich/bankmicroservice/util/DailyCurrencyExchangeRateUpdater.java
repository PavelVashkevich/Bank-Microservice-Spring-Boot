package com.github.pavelvashkevich.bankmicroservice.util;

import com.github.pavelvashkevich.bankmicroservice.model.cassandra.CurrencyExchange;
import com.github.pavelvashkevich.bankmicroservice.model.cassandra.CurrencyExchangeKey;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Exchange;
import com.github.pavelvashkevich.bankmicroservice.service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DailyCurrencyExchangeRateUpdater {
    private static final long ONE_DAY = 1;

    private final CurrencyExchangeService currencyExchangeService;
    private final TwelveApiUtil twelveApiUtil;

    @PostConstruct
    public void init () {
        // TODO
        // init first value to DB
    }

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
                    CurrencyExchangeKey currencyExchangeKey = buildCurrencyExchangeKey(exchange, getTodayDate());
                    CurrencyExchange currencyExchangeToUpdate = currencyExchangeService.findById(currencyExchangeKey);
                    Optional<BigDecimal> rateOnClose = twelveApiUtil.getRateOnCloseForSpecificExchange(exchange);
                    rateOnClose.ifPresent(currencyExchangeToUpdate::setRate);
                    return currencyExchangeToUpdate;
                }).collect(Collectors.toList());
        currencyExchangeService.updateAll(updatedCurrencyExchangesWithCloseRate);
    }

    private CurrencyExchange createCurrencyExchangeWithRateOnPreviousClose(Exchange exchange) {
        CurrencyExchange previousDayCurrencyExchange = getPreviousDayCurrencyExchange(exchange);
        CurrencyExchange newDayCurrencyExchange = new CurrencyExchange();
        newDayCurrencyExchange.setCurrencyExchangeKey(buildCurrencyExchangeKey(exchange, getTodayDate()));
        newDayCurrencyExchange.setRate(previousDayCurrencyExchange.getRate());
        return newDayCurrencyExchange;
    }

    private CurrencyExchange getPreviousDayCurrencyExchange(Exchange exchange) {
        CurrencyExchangeKey currencyExchangeKey = buildCurrencyExchangeKey(exchange, getYesterdayDate());
        return currencyExchangeService.findById(currencyExchangeKey);
    }

    private CurrencyExchangeKey buildCurrencyExchangeKey(Exchange exchange, LocalDate date) {
        return CurrencyExchangeKey
                .builder()
                .symbol(exchange.getSymbol())
                .exchangeDate(date)
                .build();
    }

    private LocalDate getTodayDate() {
        return LocalDate.now();
    }

    private LocalDate getYesterdayDate() {
        return LocalDate.now().minusDays(ONE_DAY);
    }
}