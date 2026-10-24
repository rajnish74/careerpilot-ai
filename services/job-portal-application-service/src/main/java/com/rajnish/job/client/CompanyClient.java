package com.rajnish.job.client;

import com.rajnish.job.dto.response.CompanyResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CompanyClient {

    private final RestClient restClient;

    public CompanyClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:5002")
                .build();
    }

    public CompanyResponse getCompanyById(Long companyId) {
        return restClient.get()
                .uri("/api/companies/{companyId}", companyId)
                .retrieve()
                .body(CompanyResponse.class);
    }
}
