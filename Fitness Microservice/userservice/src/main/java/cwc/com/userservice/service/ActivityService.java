package cwc.com.userservice.service;

import cwc.com.userservice.dto.ActivityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final WebClient activityServiceWebClient;

    public List<ActivityResponse> getAllActivities(String userId) {
        try {
            List<ActivityResponse> result = activityServiceWebClient.get()
                    .uri("/user/{userId}", userId)
                    .retrieve()
                    .onStatus(
                            status -> status.value() == 404,
                            clientResponse -> Mono.empty()
                    )
                    .bodyToFlux(ActivityResponse.class)
                    .collectList()
                    .block();

            return result != null ? result : Collections.emptyList();

        } catch (WebClientResponseException.NotFound ex) {
            return Collections.emptyList();
        }
    }

}
