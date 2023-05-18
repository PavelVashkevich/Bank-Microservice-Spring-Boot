package com.github.pavelvashkevich.bankmicroservice.util;

import com.github.pavelvashkevich.bankmicroservice.exception.RemoteAPIAccessException;
import com.github.pavelvashkevich.bankmicroservice.handler.RestTemplateResponseErrorHandler;
import com.github.pavelvashkevich.bankmicroservice.model.types.EndOfDayTwelveApiExchangeData;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class TwelveApiService {
    private final static String END_OF_DAY_URL = "https://api.twelvedata.com/eod?symbol=%s&apikey=%s";

    private final RestTemplate restTemplate;

    @Value("${twelvdate.apiKey}")
    private String apiKey;


    @Autowired
    public TwelveApiService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @Retryable(recover = "getEndOfDayTwelveApiExchangeDataRecovery", include = {RemoteAPIAccessException.class,
            ResourceAccessException.class}, maxAttemptsExpression = "#{${twelvdate.maxConnectionRetryAttempts}}",
            backoff = @Backoff(delayExpression = "#{${twelvdate.minDelayBetweenAttempts}}",
                    maxDelayExpression = "#{${twelvdate.maxDelayBetweenAttempts}}"))
    public Optional<EndOfDayTwelveApiExchangeData> getEndOfDayTwelveApiExchangeData(Exchange exchange) {
        // TODO log.info(Sending GET request)
        return Optional.ofNullable(restTemplate.getForObject
                (buildEndOfDayTwelveApiExchangeDataGetRequestUrl(exchange), EndOfDayTwelveApiExchangeData.class));
    }

    @Recover
    private Optional<EndOfDayTwelveApiExchangeData> getEndOfDayTwelveApiExchangeDataRecovery(Throwable cause,
                                                                                             Exchange exchange) {
        // TODO log.error() Remote API doesn't response; Empty data
        return Optional.empty();
    }

    private String buildEndOfDayTwelveApiExchangeDataGetRequestUrl(Exchange exchange) {
        return String.format(END_OF_DAY_URL, exchange.getSymbol(), apiKey);
    }
}
