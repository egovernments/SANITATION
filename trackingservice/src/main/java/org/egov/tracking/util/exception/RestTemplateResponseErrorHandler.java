package org.egov.tracking.util.exception;

import java.io.IOException;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import lombok.extern.slf4j.Slf4j;

// Common error handling component for Spring RestTemplate
@Component
@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatusCode statusCode = httpResponse.getStatusCode();
        return statusCode.is4xxClientError() || statusCode.is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatusCode statusCode = httpResponse.getStatusCode();
        if (statusCode.is5xxServerError()) {
            // Handle server errors (5xx)
            log.error("Server error occurred: {}", statusCode);
            // Add specific handling logic for server errors, if needed
        } else if (statusCode.is4xxClientError()) {
            // Handle client errors (4xx)
            log.error("Client error occurred: {}", statusCode);
            // Add specific handling logic for client errors, if needed
        }
    }
}
