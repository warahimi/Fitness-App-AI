package com.cwc.aiservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {
    private final WebClient webClient;
    @Value("${gemini.api.url}")
    private String geminiAPIUrl;
    @Value("${gemini.api.key}")
    private String geminiAPIKey;

    public String getGeminiResponse(String prompt) {
        /*
         {
            "contents": [
              {
                "parts": [
                  {
                    "text": "Write me a 10 line paragraph about Graphs in Computer Science"
                  }
                ]
              }
            ]
          }
         */
        Map<String, Object> requestBody = Map.of("contents", new Object[]{
                Map.of("parts", new Object[]{
                        Map.of("text", prompt)
                })
        });


        return webClient.post()
                .uri(geminiAPIUrl+geminiAPIKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
