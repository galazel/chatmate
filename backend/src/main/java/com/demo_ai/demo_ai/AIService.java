package com.demo_ai.demo_ai;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class AIService {

    private final WebClient webClient;
    private AIRepository repository;

    public AIService(WebClient webClient, AIRepository repository) {
        this.webClient = webClient;
        this.repository = repository;
    }

    public String askAI(AIDTO dto) {
        String prompt = dto.getRequest();
        String requestBody = """
        {
          "model": "gpt-4o-mini",
          "messages": [
            { "role": "user", "content": "%s" }
          ]
        }
        """.formatted(prompt);

        JsonNode jsonResponse = webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        String answer = jsonResponse.get("choices").get(0).get("message").get("content").asText();

        repository.save(AI.builder()
                        .userId(dto.getId())
                        .request(dto.getRequest())
                .response(answer)
                .build());

        return answer;
    }
}
