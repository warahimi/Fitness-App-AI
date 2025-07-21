package cwc.com.userservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import cwc.com.userservice.model.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityResponse {
    private String id;
    private String userId;
    private ActivityType activityType;
    private Integer duration;
    private Integer caloriesBurned;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy' 'HH:mm:ss")
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrics;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy' 'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy' 'hh:mm a")
    private LocalDateTime updatedAt;
}
