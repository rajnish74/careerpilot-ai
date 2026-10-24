package com.rajnish.job.client;

import com.rajnish.job.dto.response.UserResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:5001")
                .build();
    }

    public UserResponse getUserById(Long userId) {
        return restClient.get()
                .uri("/api/users/{userId}", userId)
                .retrieve()
                .body(UserResponse.class);
    }
}
