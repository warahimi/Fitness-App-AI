package cwc.com.activityservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import cwc.com.activityservice.model.ActivityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;


import java.time.LocalDateTime;
import java.util.Map;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ActivityRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Activity type is required")
    private ActivityType activityType;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be a positive number")
    private Integer duration;

    @NotNull(message = "Calories burned is required")
    @Positive(message = "Calories burned must be a positive number")
    private Integer caloriesBurned;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy' 'HH:mm:ss")
    private LocalDateTime startTime;

    private Map<String, Object> additionalMetrics;
}

