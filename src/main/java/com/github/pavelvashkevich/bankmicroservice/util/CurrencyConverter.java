package com.github.pavelvashkevich.bankmicroservice.util;

import com.github.pavelvashkevich.bankmicroservice.exception.UnsupportedCurrencyConversionException;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Currency;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Exchange;
import com.github.pavelvashkevich.bankmicroservice.service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The CurrencyConverter class provides methods for converting currencies.
 * It relies on the CurrencyExchangeService for obtaining exchange rates.
 *
 * @author paulvashkevich@gmail.com
 */
@Component
@RequiredArgsConstructor
public class CurrencyConverter {
    private static final int SCALE = 2;
    private static final String UNSUPPORTED_CONVERSION_MSG = "%s conversions in not supported.";

    private final CurrencyExchangeService currencyExchangeService;


    public BigDecimal covertSumToUsd(Currency currency, BigDecimal sum) {
        switch (currency) {
            case KZT:
                return convert(Exchange.USD_TO_KZT, sum);
            case RUB:
                return convert(Exchange.USD_TO_RUB, sum);
            default:
                throw new UnsupportedCurrencyConversionException(
                        String.format(UNSUPPORTED_CONVERSION_MSG, currency.name()));
        }
    }

    private BigDecimal convert(Exchange exchange, BigDecimal sum) {
        BigDecimal currentExchangeRate = currencyExchangeService.getCurrentExchangeRate(exchange);
        return sum.divide(currentExchangeRate, SCALE, RoundingMode.HALF_UP);
    }
}
