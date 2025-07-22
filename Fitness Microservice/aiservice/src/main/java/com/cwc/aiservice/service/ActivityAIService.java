package com.cwc.aiservice.service;

import com.cwc.aiservice.model.Activity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/*
    This class will work with AI models / Gemini Server to generate activity suggestions
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {
    private final GeminiService geminiService;
    public String generateActivityRecommendation(Activity activity)
    {
        String prompt = getnerateActivityPrompt(activity);
        String AIResponse = geminiService.getGeminiResponse(prompt);
        log.info("AI Response: {}", AIResponse);
        processAIResponse(activity, AIResponse);
        return AIResponse;
    }

    private void processAIResponse(Activity activity, String AIResponse)
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(AIResponse);
            /*
            Traversing over the response which has the following structure
            {
                "candidates": [
                    {
                        "content": {
                            "parts": [
                                {
                                    "text": "Graphs, in computer science, are abstract data structures composed of nodes (vertices) connected by edges. They are invaluable for representing relationships and networks, modeling diverse systems like social networks, transportation routes, and dependencies between tasks. Unlike linear structures like arrays, graphs can represent complex, non-hierarchical relationships.  Graph theory provides algorithms for traversing, searching, and analyzing these networks, solving problems such as finding shortest paths (Dijkstra's algorithm), detecting cycles, and determining connectivity. Common graph representations include adjacency matrices and adjacency lists, each with their own trade-offs regarding space and time complexity.  The choice of representation and algorithm depends heavily on the specific problem and the characteristics of the graph itself. They are widely used in network analysis, social science, cheminformatics, and operations research. Their flexible structure and power to model connections make them a cornerstone of computer science.\n"
                                }
                            ],
                            "role": "model"
                        },
                        "finishReason": "STOP",
                        "avgLogprobs": -0.56614077293266685
                    }
                ],
                "usageMetadata": {
                    "promptTokenCount": 13,
                    "candidatesTokenCount": 177,
                    "totalTokenCount": 190,
                    "promptTokensDetails": [
                        {
                            "modality": "TEXT",
                            "tokenCount": 13
                        }
                    ],
                    "candidatesTokensDetails": [
                        {
                            "modality": "TEXT",
                            "tokenCount": 177
                        }
                    ]
                },
                "modelVersion": "gemini-2.0-flash",
                "responseId": "HtZ-aI3LKsTp1PIPxfDn6QQ"
            }
            * */
            JsonNode analysisNode = rootNode.path("candidates")
                    .get(0) // get the first element in candidates array
                    .path("content")
                    .get("parts")
                    .get(0) // get the first part
                    .path("text"); // get the text content
            String JsonContent = analysisNode.asText()
                    .replaceAll("```json\\n","")
                    .replaceAll("\\n```","")
                    .trim();
            log.info("Parsed JSON Content: {}", JsonContent);
        }catch (Exception e) {
            log.error("Error processing AI response for activity {}: {}", activity.getId(), e.getMessage());
            throw new RuntimeException("Failed to process AI response: " + e.getMessage());
        }
    }

    private String getnerateActivityPrompt(Activity activity) {
        return String.format("""
        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
        {
          "analysis": {
            "overall": "Overall analysis here",
            "pace": "Pace analysis here",
            "heartRate": "Heart rate analysis here",
            "caloriesBurned": "Calories analysis here"
          },
          "improvements": [
            {
              "area": "Area name",
              "recommendation": "Detailed recommendation"
            }
          ],
          "suggestions": [
            {
              "workout": "Workout name",
              "description": "Detailed workout description"
            }
          ],
          "safety": [
            "Safety point 1",
            "Safety point 2"
          ]
        }

        Analyze this activity:
        Activity Type: %s
        Duration: %d minutes
        Calories Burned: %d
        Additional Metrics: %s
        
        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
        Ensure the response follows the EXACT JSON format shown above.
        """,
                activity.getActivityType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }

}
