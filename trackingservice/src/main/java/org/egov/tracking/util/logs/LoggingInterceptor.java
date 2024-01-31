package org.egov.tracking.util.logs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Log request details
        log.info("Received request: {} {} from {}", request.getRequestURL(), request.getQueryString(), request.getRemoteAddr());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Log response details
        log.info("Sent response: {} {} with status {} and exception {}", request.getMethod(), request.getRequestURI(), response.getStatus(), ex);
    }
}