package com.cwc.aiservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
@Document(collection = "recommendations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recommendation {
    @Id
    private String id;
    private String activityId;
    private String userId;
    private String activityType;
    private String recommendation;
    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safetyTips;
    @CreatedDate
    private LocalDateTime createdAt;
}
