package com.ahnis.servaia.notification.config;

import com.sendgrid.SendGrid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class SendGridConfig {
    private final SendGridProperties sendGridProperties;

    @Bean
    @Primary
    public SendGrid sendGrid() {
        return new SendGrid(sendGridProperties.apiKey());
    }
}
