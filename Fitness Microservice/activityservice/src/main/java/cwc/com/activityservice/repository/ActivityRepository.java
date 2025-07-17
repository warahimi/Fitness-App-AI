package cwc.com.activityservice.repository;

import cwc.com.activityservice.model.Activity;
import cwc.com.activityservice.model.ActivityType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {
    List<Activity> findByUserId(String userId);
//    List<Activity> findByType(ActivityType type);
}
