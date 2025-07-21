package cwc.com.userservice.util;

import cwc.com.userservice.dto.ActivityResponse;
import cwc.com.userservice.dto.UserRequest;
import cwc.com.userservice.dto.UserResponse;
import cwc.com.userservice.entity.User;
import cwc.com.userservice.service.ActivityService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class Util {
    private final ActivityService activityService;

    private static ActivityService staticActivityService;

    @PostConstruct
    public void init(){
        staticActivityService = activityService;
    }
    public static UserResponse userToUserResponse(User user) {
        if (user == null) {
            return null;
        }
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setUserRole(user.getUserRole());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        List<ActivityResponse> allActivities = staticActivityService.getAllActivities(user.getId());
        if (allActivities == null || allActivities.isEmpty()) {
            userResponse.setActivities(new ArrayList<>());
            return userResponse;
        }
        userResponse.setActivities(allActivities);
        return userResponse;

    }
    public static User userRquestToUser(UserRequest userRequest)
    {
        if (userRequest == null) {
            return null;
        }
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        return user;
    }
}
