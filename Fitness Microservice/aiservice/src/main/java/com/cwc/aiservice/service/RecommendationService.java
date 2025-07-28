package com.cwc.aiservice.service;

import com.cwc.aiservice.model.Recommendation;
import com.cwc.aiservice.repositiry.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public List<Recommendation> getUserRecommendation(String userId)
    {
        return recommendationRepository.findByUserId(userId);
    }
    public Recommendation getActivityRecommendation(String activityId)
    {
        return recommendationRepository.findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException("Recommendation not found for activity: " + activityId));
    }
    public List<Recommendation> getAllRecommendations() {
        return recommendationRepository.findAll();
    }
}
