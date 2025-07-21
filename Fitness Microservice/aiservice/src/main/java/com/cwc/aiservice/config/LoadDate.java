package com.cwc.aiservice.config;

import com.cwc.aiservice.model.Recommendation;
import com.cwc.aiservice.repositiry.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LoadDate implements CommandLineRunner {
    private final RecommendationRepository recommendationRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("-----------------------------Loading initial data into the database...");
        Recommendation recommendation = Recommendation.builder()
                .activityId("activity123")
                .userId("user456")
                .activityType("RUNNING")
                .recommendation("Increase your running distance gradually.")
                .improvements(List.of("Improve your stamina", "Increase speed"))
                .suggestions(List.of("Try interval training", "Incorporate strength training"))
                .safetyTips(List.of("Stay hydrated", "Wear proper running shoes"))
                .build();
        if (recommendationRepository.findAll().isEmpty()) {
            System.out.println("Saving initial recommendation data...");
            recommendationRepository.save(recommendation);
        } else {
            System.out.println("Recommendation data already exists, skipping save.");
        }
    }
}
