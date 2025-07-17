package cwc.com.activityservice.service;

import cwc.com.activityservice.dto.ActivityRequest;
import cwc.com.activityservice.dto.ActivityResponse;
import cwc.com.activityservice.model.Activity;
import cwc.com.activityservice.repository.ActivityRepository;
import cwc.com.activityservice.utilities.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityResponse getActivityById(String id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + id));
        return Util.activityToActivityResponse(activity);
    }

    public ActivityResponse addActivity(ActivityRequest activityRequest) {
        Activity activity = Util.activityRequestToActivity(activityRequest);
        Activity savedActivity = activityRepository.save(activity);
        return Util.activityToActivityResponse(savedActivity);
    }
    public List<ActivityResponse> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return activities.stream()
                .map(Util::activityToActivityResponse)
                .toList();
    }

    public void deleteActivity(String id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + id));
        activityRepository.delete(activity);
    }
    public ActivityResponse updateActivity(String id, ActivityRequest activityRequest) {
        Activity existingActivity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + id));

        existingActivity.setUserId(activityRequest.getUserId());
        existingActivity.setActivityType(activityRequest.getActivityType());
        existingActivity.setDuration(activityRequest.getDuration());
        existingActivity.setCaloriesBurned(activityRequest.getCaloriesBurned());
        existingActivity.setStartTime(activityRequest.getStartTime());
        existingActivity.setAdditionalMetrics(activityRequest.getAdditionalMetrics());

        Activity updatedActivity = activityRepository.save(existingActivity);
        return Util.activityToActivityResponse(updatedActivity);
    }
    public List<ActivityResponse> getActivitiesByUserId(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        return activities.stream()
                .map(Util::activityToActivityResponse)
                .toList();
    }
}
