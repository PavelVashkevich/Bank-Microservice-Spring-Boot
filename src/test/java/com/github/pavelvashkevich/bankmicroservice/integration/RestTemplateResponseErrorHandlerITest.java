package com.github.pavelvashkevich.bankmicroservice.integration;

import com.github.pavelvashkevich.bankmicroservice.exception.RemoteAPIAccessException;
import com.github.pavelvashkevich.bankmicroservice.handler.RestTemplateResponseErrorHandler;
import com.github.pavelvashkevich.bankmicroservice.model.types.EndOfDayTwelveApiExchangeData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

@RestClientTest
public class RestTemplateResponseErrorHandlerITest {
    @Autowired
    private MockRestServiceServer serviceServer;
    @Autowired
    private RestTemplateBuilder builder;

    @Test
    public void givenRemoteApiCall_whenNotFoundResponse_thenTrowException() {
        RestTemplate template = this.builder.errorHandler(new RestTemplateResponseErrorHandler()).build();

        this.serviceServer.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo("/foo/1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

        Assertions.assertThrows(RemoteAPIAccessException.class, () -> {
            EndOfDayTwelveApiExchangeData data = template.getForObject("/foo/1", EndOfDayTwelveApiExchangeData.class);
        });
    }
}
