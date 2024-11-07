package com.d205.KIWI_Backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs()
                .maxInMemorySize(10 * 1024 * 1024)) // 10MB로 버퍼 크기 증가
            .build();

        return builder
            .baseUrl("http://localhost:9200")
            .exchangeStrategies(exchangeStrategies)
            .build();
    }
}
