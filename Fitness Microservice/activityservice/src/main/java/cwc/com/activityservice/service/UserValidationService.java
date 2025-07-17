package cwc.com.activityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final WebClient userServiceWebClient;

    public boolean isValidUser(String userId) {
        return Boolean.TRUE.equals(userServiceWebClient.get()
                .uri("/validate/{userId}", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());
    }
}
