package com.ahnis.servaia;

import org.springframework.ai.autoconfigure.vectorstore.milvus.MilvusVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.TimeZone;

@SpringBootApplication(exclude = MilvusVectorStoreAutoConfiguration.class)
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@EnableMongoRepositories
@EnableMongoAuditing

public class ServaiaApplication {

    public static void main(String[] args) {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        SpringApplication.run(ServaiaApplication.class, args);
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

    }

}
