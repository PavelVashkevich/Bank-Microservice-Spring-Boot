package com.github.pavelvashkevich.bankmicroservice.util;

import com.github.pavelvashkevich.bankmicroservice.exception.UnsupportedCurrencyConversionException;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Currency;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Exchange;
import com.github.pavelvashkevich.bankmicroservice.service.CurrencyExchangeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CurrencyConverterTest {
    @Mock
    private CurrencyExchangeService exchangeService;
    @InjectMocks
    private CurrencyConverter currencyConverter;


    @DisplayName("JUnit test with KZT currency for the 'convertToUsd' method of the CurrencyConverter class")
    @ParameterizedTest
    @CsvFileSource(resources = "/kztsumdataset.csv",numLinesToSkip = 1)
    public void givenKZTCurrencyAndSum_whenConvertToUsd_thenVerifyTheResult(BigDecimal sum, String expected) {
        Currency currency = Currency.KZT;
        given(exchangeService.getCurrentExchangeRate(Exchange.USD_TO_KZT))
                .willReturn(BigDecimal.valueOf(400L));

        BigDecimal result = currencyConverter.covertSumToUsd(currency, sum);

        then(exchangeService).should().getCurrentExchangeRate(Exchange.USD_TO_KZT);
        assertThat(result).isEqualTo(new BigDecimal(expected));
    }

    @DisplayName("JUnit test with KZT currency for the 'convertToUsd' method of the CurrencyConverter class")
    @ParameterizedTest
    @CsvFileSource(resources = "/rubsumdataset.csv",numLinesToSkip = 1)
    public void givenRUBCurrencyAndSum_whenConvertToUsd_thenVerifyTheResult(BigDecimal sum, String expected) {
        Currency currency = Currency.RUB;
        given(exchangeService.getCurrentExchangeRate(Exchange.USD_TO_RUB))
                .willReturn(BigDecimal.valueOf(65.32));

        BigDecimal result = currencyConverter.covertSumToUsd(currency, sum);

        then(exchangeService).should().getCurrentExchangeRate(Exchange.USD_TO_RUB);
        assertThat(result).isEqualTo(new BigDecimal(expected));
    }

    @DisplayName("JUnit test with unsupported currency for the 'convertToUsd' method of the CurrencyConverter class")
    @Test
    public void givenUnsupportedCurrencyAndSum_whenConvertToUsd_thenVerifyExceptionRaised() {
        Currency currency = Currency.USD;
        BigDecimal sum = BigDecimal.ONE;

        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedCurrencyConversionException.class, ()
                -> currencyConverter.covertSumToUsd(currency, sum));
    }
}