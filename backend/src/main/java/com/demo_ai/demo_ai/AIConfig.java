package com.demo_ai.demo_ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AIConfig {

    @Value("${spring.data.github-token}")
    private String githubToken;
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://models.github.ai/inference")
                .defaultHeader("Authorization", "Bearer " + githubToken)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
