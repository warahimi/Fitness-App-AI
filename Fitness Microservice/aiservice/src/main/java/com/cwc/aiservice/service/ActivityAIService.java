package com.cwc.aiservice.service;

import com.cwc.aiservice.model.Activity;
import com.cwc.aiservice.model.Recommendation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
    This class will work with AI models / Gemini Server to generate activity suggestions
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {
    private final GeminiService geminiService;
    public Recommendation generateActivityRecommendation(Activity activity)
    {
        String prompt = getnerateActivityPrompt(activity);
        String AIResponse = geminiService.getGeminiResponse(prompt);
        log.info("AI Response: {}", AIResponse);
        return processAIResponse(activity, AIResponse);
    }

    private Recommendation processAIResponse(Activity activity, String AIResponse)
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
            JsonNode jsonNode = rootNode.path("candidates")
                    .get(0) // get the first element in candidates array
                    .path("content")
                    .get("parts")
                    .get(0) // get the first part
                    .path("text"); // get the text content
            String JsonContent = jsonNode.asText()
                    .replaceAll("```json\\n","")
                    .replaceAll("\\n```","")
                    .trim();
//            log.info("Parsed JSON Content: {}", JsonContent);

            /*
            JsonContent looks like this
            {
                "analysis": {
                    "overall": "This run shows a good start to a fitness routine, covering a decent distance in a moderate timeframe. However, the calories burned seems low for the distance and duration, suggesting room for improvement in intensity or metabolic efficiency. The elevation gain is a positive aspect, indicating the run included varied terrain which can contribute to improved strength and endurance. Weight lost is minimal, expected for a single short run.",
                    "pace": "The pace is approximately 8 minutes per kilometer (40 minutes / 5 km = 8 minutes/km). This is a moderate pace, suitable for building endurance. Analyzing pace variation during the run (if available) would provide further insights into consistency and potential fatigue.",
                    "heartRate": "Without heart rate data, it's difficult to assess the intensity level accurately. Ideally, heart rate zones should be monitored to ensure the run falls within the desired aerobic or anaerobic training range. Calorie expenditure is heavily influenced by the average heart rate achieved during the activity.",
                    "caloriesBurned": "200 calories burned seems relatively low for a 5 km run lasting 40 minutes. This suggests either a very low intensity (low heart rate) or potentially an inaccurate calculation. Consider improving the accuracy of your fitness tracker or consulting with a fitness professional to better estimate calorie expenditure. Body weight significantly impacts calorie burn, ensure it is correctly recorded."
                },
                "improvements": [
                    {
                        "area": "Intensity",
                        "recommendation": "Increase the intensity of your runs. Try incorporating interval training, where you alternate between high-intensity bursts and periods of rest or low-intensity activity. This can help improve your cardiovascular fitness and calorie burn."
                    },
                    {
                        "area": "Calorie Calculation Accuracy",
                        "recommendation": "Ensure your fitness tracker settings (weight, height, age) are accurate. If using an app, calibrate it with a treadmill test or compare it to other methods of calorie expenditure estimation."
                    },
                    {
                        "area": "Heart Rate Monitoring",
                        "recommendation": "Use a heart rate monitor to track your heart rate during runs. This will help you understand the intensity of your workouts and ensure you're training in the right heart rate zones to achieve your fitness goals. Aim for Zone 2 (60-70% of max HR) for endurance building."
                    }
                ],
                "suggestions": [
                    {
                        "workout": "Fartlek Training",
                        "description": "A Fartlek workout involves alternating between different speeds and intensities throughout your run. For example, sprint for 30 seconds, then jog for 1 minute, then run at a moderate pace for 2 minutes. This type of training can help improve your speed, endurance, and overall fitness."
                    },
                    {
                        "workout": "Hill Repeats",
                        "description": "Find a moderately steep hill and run up it at a hard effort. Jog back down to recover, and repeat several times. This will build strength and power in your legs, and improve your cardiovascular fitness."
                    },
                    {
                        "workout": "Longer Run",
                        "description": "Increase your running distance gradually each week. Start by adding 10% to your current longest run distance. This will help build your endurance and prepare you for longer races or runs."
                    }
                ],
                "safety": [
                    "Warm up before each run with dynamic stretches, such as leg swings and arm circles.",
                    "Cool down after each run with static stretches, such as hamstring stretches and calf stretches.",
                    "Stay hydrated by drinking water before, during, and after your runs.",
                    "Wear appropriate running shoes to prevent injuries.",
                    "Be aware of your surroundings, especially when running in traffic or on trails.",
                    "Listen to your body and rest when needed. Don't push yourself too hard, especially when starting out."
                ]
            }

            now we are going to parse this JSON content to the Recommendation model
             */
            JsonNode analysisJson = objectMapper.readTree(JsonContent);

            //Analyze the "analysis"
            JsonNode analysisNode = analysisJson.path("analysis");

            StringBuilder overallAnalysis = new StringBuilder();

            addAnalysisSection(overallAnalysis, analysisNode, "overall", "Overall: ");
            addAnalysisSection(overallAnalysis, analysisNode, "pace", "Pace: ");
            addAnalysisSection(overallAnalysis, analysisNode, "heartRate", "Heart Rate: ");
            addAnalysisSection(overallAnalysis, analysisNode, "caloriesBurned", "Calories Burned: ");

            // getting the improvements out of the analysisJson
            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            // getting the suggestions out of the analysisJson
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));
            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getActivityType().toString())
                    .recommendation(overallAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safetyTips(safety)
                    .build();



        }catch (Exception e) {
            log.error("Error processing AI response for activity {}: {}", activity.getId(), e.getMessage());
//            throw new RuntimeException("Failed to process AI response: " + e.getMessage());
            e.printStackTrace();
            return createDefaultRecommendation(activity);
        }
    }
    private Recommendation createDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getActivityType().toString())
                .recommendation("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue with your current routine"))
                .suggestions(Collections.singletonList("Consider consulting a fitness professional"))
                .safetyTips(Arrays.asList(
                        "Always warm up before exercise",
                        "Stay hydrated",
                        "Listen to your body"
                ))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safetyList = new ArrayList<>();
        if(safetyNode.isArray())
        {
            for (JsonNode safetyItem : safetyNode) {
                safetyList.add(safetyItem.asText());
            }
            return safetyList;
        }
        return safetyList.isEmpty() ? List.of("No safety guidelines provided") : safetyList;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestionsList = new ArrayList<>();
        if(suggestionsNode.isArray())
        {
            for (JsonNode suggestion : suggestionsNode) {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestionsList.add(String.format("%s: %s", workout, description));
            }
        }
        return suggestionsList.isEmpty() ? List.of("No suggestions provided") : suggestionsList;
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        /*
        Argument looks like
           "improvements": [
                    {
                        "area": "Intensity",
                        "recommendation": "Increase the intensity of your runs. Try incorporating interval training, where you alternate between high-intensity bursts and periods of rest or low-intensity activity. This can help improve your cardiovascular fitness and calorie burn."
                    },
                    {
                        "area": "Calorie Calculation Accuracy",
                        "recommendation": "Ensure your fitness tracker settings (weight, height, age) are accurate. If using an app, calibrate it with a treadmill test or compare it to other methods of calorie expenditure estimation."
                    },
                    {
                        "area": "Heart Rate Monitoring",
                        "recommendation": "Use a heart rate monitor to track your heart rate during runs. This will help you understand the intensity of your workouts and ensure you're training in the right heart rate zones to achieve your fitness goals. Aim for Zone 2 (60-70% of max HR) for endurance building."
                    }
                ]
         */
        List<String> improvementsList = new ArrayList<>();
        if(improvementsNode.isArray())
        {
            for (JsonNode improvement : improvementsNode) {
                String area = improvement.path("area").asText();
                String recommendation = improvement.path("recommendation").asText();
                improvementsList.add(String.format("%s: %s", area, recommendation));
            }
            return improvementsList;
        }
        return improvementsList.isEmpty() ? List.of("No improvements suggested") : improvementsList;
    }

    private void addAnalysisSection(StringBuilder overallAnalysis, JsonNode analysisNode, String key, String prefix) {
        overallAnalysis.append(prefix)
                .append(analysisNode.path(key).asText())
                .append("\n\n");
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
