package cwc.com.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient activityServiceWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8082/api/v1/activities")
                .build();
    }
}
