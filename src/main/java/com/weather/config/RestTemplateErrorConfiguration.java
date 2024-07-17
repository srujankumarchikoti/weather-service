package com.weather.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.exception.ResourceDoesNotExistException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;

@Slf4j
public class RestTemplateErrorConfiguration implements ResponseErrorHandler {

    private final ObjectMapper objectMapper;

    public RestTemplateErrorConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return (clientHttpResponse
                .getStatusCode()
                .is5xxServerError()
                || clientHttpResponse
                .getStatusCode()
                .is4xxClientError());
    }

    @SneakyThrows
    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) {
        int httpStatusCode = clientHttpResponse
                .getStatusCode()
                .value();

        if (clientHttpResponse
                .getStatusCode()
                .is5xxServerError()) {
            log.error("Received error response with status code: {}", httpStatusCode);
        }

        if (httpStatusCode == HttpStatus.NOT_FOUND.value()) {
            throw new ResourceDoesNotExistException();
        }

        final Scanner scanner = new Scanner(clientHttpResponse.getBody()).useDelimiter("\\A");
        final String downStreamErrorResponse = scanner.hasNext() ? scanner.next() : "";
        log.error(downStreamErrorResponse);
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse = objectMapper.readValue(downStreamErrorResponse, errorResponse.getClass());
        String errorMessage = errorResponse.get("message");
        if (StringUtils.isEmpty(errorMessage)) {
            log.info("Error message not found.");
        }
        if (clientHttpResponse
                .getStatusCode()
                .is4xxClientError()) {
            throw new RuntimeException(errorMessage);
        } else if (clientHttpResponse
                .getStatusCode()
                .is5xxServerError()) {
            throw new RuntimeException(errorMessage);
        }
    }
}
