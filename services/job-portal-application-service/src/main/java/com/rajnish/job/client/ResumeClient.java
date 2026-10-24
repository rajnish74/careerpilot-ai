package com.rajnish.job.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ResumeClient {

    private final RestClient restClient;

    public ResumeClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:5006")
                .build();
    }

    public Object getResumeByUserId(Long userId) {
        return restClient.get()
                .uri("/api/resumes/user/{userId}", userId)
                .retrieve()
                .body(Object.class);
    }
}
