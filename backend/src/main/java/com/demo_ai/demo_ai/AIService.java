package com.demo_ai.demo_ai;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class AIService {

    private final WebClient webClient;
    private AIRepository repository;

    public AIService(WebClient webClient, AIRepository repository) {
        this.webClient = webClient;
        this.repository = repository;
    }

    public String askAI(AIDTO dto) {

        String projectData = """
        PROJECT: Villa Sunshine
        CLIENT: Mr. Santos
        CONTRACTOR: Rico (that's you)
        
        === OVERALL PROGRESS ===
        Current Stage: Roofing and External Walls
        Overall Completion: ~55%%
        Expected Handover: June 15, 2026
        
        === FOUNDATION & STRUCTURE (DONE) ===
        - Foundation poured and fully cured
        - Ground floor slab completed
        - All structural columns up
        
        === PLUMBING ===
        - Rough-in for all bathrooms done
        - Kitchen plumbing rough-in done
        - Water supply lines installed
        
        === BATHROOM PROGRESS ===
        Main Bathroom (Ground Floor):
          - Rough-in: DONE
          - Waterproofing: DONE
          - Floor tiling: 80%% done, just the corner cuts left
          - Wall tiling: 50%% done, waiting for more tiles (delivery tomorrow)
          - Toilet and sink: not yet installed, scheduled next week
          - Shower enclosure: frame installed, glass panels on order
          - Estimated completion: end of next week
        
        Master Bathroom (2nd Floor):
          - Rough-in: DONE
          - Waterproofing: IN PROGRESS (50%% done)
          - Tiling: not started yet
          - Fixtures: not yet installed
          - Estimated completion: 3 weeks from now
        
        Guest Bathroom (2nd Floor):
          - Rough-in: DONE
          - Waterproofing: not started
          - Everything else: not started
          - Estimated completion: 4 weeks from now
        
        === ELECTRICAL ===
        - Conduit installation: DONE
        - Wiring: 70%% done
        - Bathroom exhaust fan wiring: done for ground floor, pending 2nd floor
        
        === CURRENT ISSUES / DELAYS ===
        - Wall tiles for main bathroom delayed by 1 day (arriving tomorrow morning)
        - Shower glass panels: 3-day lead time from supplier
        - 2nd floor waterproofing slowed due to humid weather this week
        
        === NEXT STEPS THIS WEEK ===
        - Finish ground floor bathroom tiling once tiles arrive
        - Continue 2nd floor waterproofing
        - Install toilet and sink in main bathroom by Friday
        """;

        String prompt = """
        You are Rico, a friendly and experienced construction contractor chatting casually with your client Mr. Santos via text message.
        The client just asked you: "%s"
        
        Here is the current project data you should base your answer on:
        %s
        
        Instructions:
        - ONLY answer based on the project data provided above
        - If the question is NOT covered in the data, respond with something like "Di ko pa updated yung info sa ganyan, let me check muna and get back to you!" — do NOT make up, assume, or guess any information not in the data
        - Reply like you're texting a client — casual, warm, honest
        - Use simple language, no technical jargon unless needed
        - Be specific with numbers and timelines from the data
        - If there's a delay or issue, be upfront but reassuring
        - Keep it conversational, like a real contractor would talk
        - Don't use bullet points or formal formatting — just natural flowing sentences
        - Sign off naturally like Rico would
        """.formatted(dto.getRequest(), projectData);

        String requestBody = """
        {
          "model": "gpt-4o-mini",
          "messages": [
            {
              "role": "system",
              "content": "You are Rico, a friendly Filipino contractor giving casual project updates to your client via chat. You only answer based on the data given to you. If something is not in the data, you admit you don't have that info yet instead of making things up."
            },
            {
              "role": "user",
              "content": "%s"
            }
          ],
          "temperature": 0.8
        }
        """.formatted(prompt.replace("\"", "\\\"").replace("\n", "\\n"));

        JsonNode jsonResponse = webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        String answer = jsonResponse
                .get("choices").get(0)
                .get("message").get("content")
                .asText();

        repository.save(AI.builder()
                .userId(dto.getId())
                .request(dto.getRequest())
                .response(answer)
                .build());

        return answer;
    }
}