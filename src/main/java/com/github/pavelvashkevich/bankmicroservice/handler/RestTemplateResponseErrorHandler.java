package com.github.pavelvashkevich.bankmicroservice.handler;

import com.github.pavelvashkevich.bankmicroservice.exception.RemoteAPIAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    private static final String EXCEPTION_MSG = "Request to the remote source failed, returned error code - %d";

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                || httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR
                || httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR){
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getBody()))) {
                String httpResponseBody = reader.lines().collect(Collectors.joining(","));
                // TODO log.error
                throw new RemoteAPIAccessException(String.format(EXCEPTION_MSG, httpResponse.getStatusCode().value()));
            }
        }
    }
}
