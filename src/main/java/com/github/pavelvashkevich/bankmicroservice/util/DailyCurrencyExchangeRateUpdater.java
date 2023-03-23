package com.github.pavelvashkevich.bankmicroservice.util;

import com.github.pavelvashkevich.bankmicroservice.dto.EndOfDayDTO;
import com.github.pavelvashkevich.bankmicroservice.model.CurrencyExchange;
import com.github.pavelvashkevich.bankmicroservice.service.CurrencyExchangeService;
import com.github.pavelvashkevich.bankmicroservice.types.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.EnumSet;

@Component
public class DailyCurrencyExchangeRateUpdater {

    @Value("${twelvdate.api_key}")
    private String API_KEY;
    private final String END_OF_DAY_URL = "https://api.twelvedata.com/eod?symbol=%s&apikey=%s";
    private final RestTemplate restTemplate = new RestTemplate();
    private final EnumSet<Exchange> exchanges = EnumSet.allOf(Exchange.class);
    private final CurrencyExchangeService currencyExchangeService;

    @Autowired
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

    @Scheduled(cron = "${rateupdater.rate.close.cron_expression}", zone = "${rateupdater.rate.close.timezone}")
    @Scheduled(fixedRate = 30000L)
    public void updateCurrencyExchangeRateValueForNewDay() {
        exchanges.forEach(this::requestRestOnCloseValue);
    }

    public void requestRestOnCloseValue(Exchange exchange) {
        String requestUrl = buildEndOfDayGetRequestUrl(exchange);
        EndOfDayDTO endOfDayDTO = restTemplate.getForObject(requestUrl, EndOfDayDTO.class);
        CurrencyExchange currencyExchangeToUpdate = currencyExchangeService.findLatestBySymbol(exchange);
        currencyExchangeToUpdate.setRate(endOfDayDTO.getClose());
        currencyExchangeService.update(currencyExchangeToUpdate);
    }

    private String buildEndOfDayGetRequestUrl(Exchange exchange) {
        return String.format(END_OF_DAY_URL, exchange.getSymbol(), API_KEY);
    }
}