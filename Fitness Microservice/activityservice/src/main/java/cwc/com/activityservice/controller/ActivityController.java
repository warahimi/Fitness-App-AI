package cwc.com.activityservice.controller;

import cwc.com.activityservice.dto.ActivityRequest;
import cwc.com.activityservice.dto.ActivityResponse;
import cwc.com.activityservice.model.ActivityType;
import cwc.com.activityservice.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/activities")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping("/test")
    public ResponseEntity<ActivityResponse> addActivityTest() {
        ActivityRequest activityRequest = ActivityRequest.builder()
                .userId("testUserId")
                .activityType(ActivityType.HIKING)
                .duration(60)
                .caloriesBurned(300)
                .startTime(LocalDateTime.now())
                .additionalMetrics(Map.of("distance", 5.0, "elevationGain", 200))
                .build();
        ActivityResponse activityResponse = activityService.addActivity(activityRequest);
        return ResponseEntity.ok(activityResponse);
    }
    @PostMapping
    public ResponseEntity<ActivityResponse> addActivity(@RequestBody @Valid ActivityRequest activityRequest) {
        ActivityResponse activityResponse = activityService.addActivity(activityRequest);
        return ResponseEntity.ok(activityResponse);
    }
    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getAllActivities()
    {
        List<ActivityResponse> activities = activityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable String id) {
        ActivityResponse activityResponse = activityService.getActivityById(id);
        return ResponseEntity.ok(activityResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable String id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponse> updateActivity(
            @PathVariable String id,
            @RequestBody @Valid ActivityRequest activityRequest) {
        ActivityResponse updatedActivity = activityService.updateActivity(id, activityRequest);
        return ResponseEntity.ok(updatedActivity);
    }
    @GetMapping("/userid")
    public ResponseEntity<List<ActivityResponse>> getActivitiesByUserId(@RequestHeader("X-USER-ID") String userId) {
        List<ActivityResponse> activities = activityService.getActivitiesByUserId(userId);
        return ResponseEntity.ok(activities);
    }
}
