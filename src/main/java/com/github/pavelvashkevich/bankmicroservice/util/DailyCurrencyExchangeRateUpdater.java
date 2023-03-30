package com.github.pavelvashkevich.bankmicroservice.util;

import com.github.pavelvashkevich.bankmicroservice.types.EndOfDayTwelveApiData;
import com.github.pavelvashkevich.bankmicroservice.model.CurrencyExchange;
import com.github.pavelvashkevich.bankmicroservice.service.CurrencyExchangeService;
import com.github.pavelvashkevich.bankmicroservice.types.Exchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.EnumSet;

@Component
public class DailyCurrencyExchangeRateUpdater {

    private final static String END_OF_DAY_URL = "https://api.twelvedata.com/eod?symbol=%s&apikey=%s";
    private final RestTemplate restTemplate = new RestTemplate();
    private final EnumSet<Exchange> exchanges = EnumSet.allOf(Exchange.class);
    private final CurrencyExchangeService currencyExchangeService;
    @Value("${twelvedate.api.key}")
    private String API_KEY;

    public DailyCurrencyExchangeRateUpdater(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    @Scheduled(cron = "${rateupdater.rate.previous.cron_expression}")
    public void addCurrencyExchangeRateForNewDay() {
        exchanges.forEach(exchange -> {
            CurrencyExchange currencyExchange = createCurrencyExchangeWithRateOnPreviousClose(exchange);
            currencyExchangeService.save(currencyExchange);
        });
    }

    @Scheduled(cron = "${rateupdater.rate.close.cron_expression}", zone = "${rateupdater.rate.close.timezone}")
    public void updateCurrencyExchangeRateValueForNewDay() {
        exchanges.forEach(this::requestRestOnCloseValue);
    }

    private void requestRestOnCloseValue(Exchange exchange) {
        String requestUrl = buildEndOfDayGetRequestUrl(exchange);
        EndOfDayTwelveApiData endOfDayTwelveApiData = restTemplate.getForObject(requestUrl, EndOfDayTwelveApiData.class);
        CurrencyExchange currencyExchangeToUpdate = currencyExchangeService.findLatestBySymbol(exchange);
        currencyExchangeToUpdate.setRate(endOfDayTwelveApiData.getClose());
        currencyExchangeService.update(currencyExchangeToUpdate);
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

    private String buildEndOfDayGetRequestUrl(Exchange exchange) {
        return String.format(END_OF_DAY_URL, exchange.getSymbol(), API_KEY);
    }
}