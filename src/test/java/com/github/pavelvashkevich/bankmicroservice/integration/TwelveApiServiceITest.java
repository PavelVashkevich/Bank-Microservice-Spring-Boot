package com.github.pavelvashkevich.bankmicroservice.integration;

import com.github.pavelvashkevich.bankmicroservice.model.types.EndOfDayTwelveApiExchangeData;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Exchange;
import com.github.pavelvashkevich.bankmicroservice.util.TwelveApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@EnableRetry
@ContextConfiguration(classes = {TwelveApiService.class})
@RestClientTest
@ActiveProfiles("test")
public class TwelveApiServiceITest {
    private static final Exchange TEST_EXCHANGE = Exchange.USD_TO_KZT;
    private static final String TEST_REMOTE_API_URL = "https://api.twelvedata.com/eod?symbol=%s&apikey=%s";

    @Autowired
    private MockRestServiceServer serviceServer;
    @Autowired
    private TwelveApiService twelveApiService;
    @Value("${twelvdate.apiKey}")
    private String apiKey;

    @Test
    public void givenRemoteApiCall_whenGetRemoteApiData_thenVerifyRemoteApiData() {
        this.serviceServer.expect(once(), requestTo(buildEndOfDayTwelveApiUrl()))
                .andExpect(method(HttpMethod.GET))
                // TODO objectMapper
                .andRespond(withSuccess("{\"symbol\":\"USD/KZT\",\"exchange\":\"Forex\",\"datetime\":\"2023-05-11\"," +
                        "\"timestamp\":1683806640,\"close\":\"444.45001\"}", MediaType.APPLICATION_JSON));

        Optional<EndOfDayTwelveApiExchangeData> data = twelveApiService.getEndOfDayTwelveApiExchangeData(TEST_EXCHANGE);
        assertThat(data).isNotEmpty();

        assertThat(data.get().getClose()).isEqualTo(BigDecimal.valueOf(444.45001));
    }

    @Test
    public void givenRemoteApiCallWithRemoteServerError_whenGetRemoteApiData_thenVerifyRemoteApiData() {
        this.serviceServer.expect(once(), requestTo(buildEndOfDayTwelveApiUrl()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withServerError());

        Optional<EndOfDayTwelveApiExchangeData> data = twelveApiService.getEndOfDayTwelveApiExchangeData(TEST_EXCHANGE);
        assertThat(data).isEmpty();
    }

    private String buildEndOfDayTwelveApiUrl() {
        return String.format(TEST_REMOTE_API_URL, TEST_EXCHANGE.getSymbol(), apiKey);
    }
}


