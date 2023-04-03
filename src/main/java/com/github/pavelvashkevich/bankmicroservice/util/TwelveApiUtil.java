package com.github.pavelvashkevich.bankmicroservice.util;

import com.github.pavelvashkevich.bankmicroservice.model.types.EndOfDayTwelveApiData;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Exchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class TwelveApiUtil {

    private final static String END_OF_DAY_URL = "https://api.twelvedata.com/eod?symbol=%s&apikey=%s";
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${twelvdate.apiKey}")
    private String API_KEY;

    public Optional<BigDecimal> getRateOnCloseForSpecificExchange(Exchange exchange) {
        return requestRateOnCloseValue(exchange);
    }

    private Optional<BigDecimal> requestRateOnCloseValue(Exchange exchange) {
        String requestUrl = buildEndOfDayGetRequestUrl(exchange);
        Optional<EndOfDayTwelveApiData> endOfDayTwelveApiData = Optional.ofNullable(
                restTemplate.getForObject(requestUrl, EndOfDayTwelveApiData.class));
        if (endOfDayTwelveApiData.isPresent()) {
            return Optional.of(endOfDayTwelveApiData.get().getClose());
        } else {
            // TODO exception or log.error
        }
        return Optional.empty();
    }

    private String buildEndOfDayGetRequestUrl(Exchange exchange) {
        return String.format(END_OF_DAY_URL, exchange.getSymbol(), API_KEY);
    }
}
