package cwc.com.activityservice.service;

import cwc.com.activityservice.dto.ActivityRequest;
import cwc.com.activityservice.dto.ActivityResponse;
import cwc.com.activityservice.exception.ActivityNotFoundException;
import cwc.com.activityservice.exception.UserNotFoundException;
import cwc.com.activityservice.model.Activity;
import cwc.com.activityservice.repository.ActivityRepository;
import cwc.com.activityservice.utilities.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;
    private final AmqpTemplate amqpTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String activityExchangeName;
    @Value("${rabbitmq.routing.name}")
    private String activityRoutingName;

    public ActivityResponse getActivityById(String id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found with id: " + id));
        return Util.activityToActivityResponse(activity);
    }

    public ActivityResponse addActivity(ActivityRequest activityRequest) {
        if(!userValidationService.isValidUser(activityRequest.getUserId())) {
            throw new UserNotFoundException("Invalid user ID: " + activityRequest.getUserId());
        }
        Activity activity = Util.activityRequestToActivity(activityRequest);

        Activity savedActivity = activityRepository.save(activity);
        // save the activity to the database and publish it to the queue
        // publish to RabbitMQ for AI processing
       try {
           log.info("Publishing activity to RabbitMQ: {}", savedActivity);
//            rabbitTemplate.convertAndSend(activityExchangeName, activityRoutingName, savedActivity);
            amqpTemplate.convertAndSend(activityExchangeName, activityRoutingName, savedActivity);
            log.info("Activity published to RabbitMQ successfully: {}", savedActivity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish activity to RabbitMQ: " + e.getMessage());
       }
        return Util.activityToActivityResponse(savedActivity);
    }
    public List<ActivityResponse> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        if (activities.isEmpty()) {
            throw new ActivityNotFoundException("No activities found.");
        }
        return activities.stream()
                .map(Util::activityToActivityResponse)
                .toList();
    }

    public void deleteActivity(String id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found with id: " + id));
        activityRepository.delete(activity);
    }
    public ActivityResponse updateActivity(String id, ActivityRequest activityRequest) {
        Activity existingActivity = activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found with id: " + id));

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
        if (activities.isEmpty()) {
            throw new ActivityNotFoundException("No activities found for user ID: " + userId);
        }
        return activities.stream()
                .map(Util::activityToActivityResponse)
                .toList();
    }
}
