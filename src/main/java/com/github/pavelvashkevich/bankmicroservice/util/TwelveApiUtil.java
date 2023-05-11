package com.github.pavelvashkevich.bankmicroservice.util;

import com.github.pavelvashkevich.bankmicroservice.model.types.EndOfDayTwelveApiExchangeData;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class TwelveApiUtil {
    private final TwelveApiService twelveApiService;

    @Autowired
    public TwelveApiUtil(TwelveApiService twelveApiService) {
        this.twelveApiService = twelveApiService;
    }

    public Optional<BigDecimal> getRateOnCloseForSpecificExchange(Exchange exchange) {
        return getRateOnCloseValue(exchange);
    }

    private Optional<BigDecimal> getRateOnCloseValue(Exchange exchange) {
        Optional<EndOfDayTwelveApiExchangeData> endOfDayTwelveApiData
                = twelveApiService.getEndOfDayTwelveApiExchangeData(exchange);
        if (endOfDayTwelveApiData.isPresent()) {
            return Optional.of(endOfDayTwelveApiData.get().getClose());
        } else {
            // TODO log.error()
        }
        return Optional.empty();
    }
}
