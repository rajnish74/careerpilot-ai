package com.rajnish.job.client;

import com.rajnish.job.dto.response.JobResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class JobClient {

    private final RestClient restClient;

    public JobClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:5003")
                .build();
    }

    public JobResponse getJobById(Long jobId) {
        return restClient.get()
                .uri("/api/jobs/{jobId}", jobId)
                .retrieve()
                .body(JobResponse.class);
    }
}
