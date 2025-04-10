package com.ahnis.servaia.common.config;


import com.ahnis.servaia.common.dto.ErrorDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.time.LocalDateTime;

@Configuration
@Slf4j
public class SecurityExceptionHandlerConfig {


    public static final String AUTH_REQUIRED_MSG = "Authentication required";
    public static final String INSUFFICIENT_PERMISSIONS_MSG = "Insufficient permissions";

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {

        return (request, response, ex) -> {
            String message;
            switch (ex) {
                case BadCredentialsException e:
                    message = "Invalid credentials";
                    break;
                default:
                    message = AUTH_REQUIRED_MSG;
                    break;
            }
            log.error("Authentication failed: {}", ex.getMessage());
            var error = new ErrorDetails(
                    LocalDateTime.now(),
                    message,
                    ex.getMessage()
            );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), error);
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper) {
        return (request, response, ex) -> {
            log.error("Access denied: {}", ex.getMessage());
            var error = new ErrorDetails(
                    LocalDateTime.now(),
                    INSUFFICIENT_PERMISSIONS_MSG,
                    ex.getMessage()
            );
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), error);
        };
    }
}
