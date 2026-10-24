package com.rajnish.job.event;

import com.rajnish.job.client.CompanyClient;
import com.rajnish.job.client.JobClient;
import com.rajnish.job.client.UserClient;
import com.rajnish.job.dto.response.CompanyResponse;
import com.rajnish.job.dto.response.JobResponse;
import com.rajnish.job.dto.response.UserResponse;
import com.rajnish.job.entity.Application;
import com.rajnish.job.enums.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ApplicationEventPublisher {

    private final UserClient userClient;
    private final CompanyClient client;
    private final JobClient jobClient;

    public static final String TOPIC = "application.status.changed";
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void publishStatusChange(Application application,
                                    ApplicationStatus oldStatus,
                                    ApplicationStatus newStatus, String note) {
        try {


            UserResponse candidate = userClient.getUserById(application.getCandidateId());
            JobResponse job = jobClient.getJobById(application.getJobId());
            CompanyResponse company = client.getCompanyById(application.getCompanyId());

            ApplicationStatusChangedEvent event = ApplicationStatusChangedEvent.builder()
                    .applicationId(application.getId())
                    .candidateId(application.getCandidateId())
                    .candidateEmail(candidate.getEmail())
                    .candidateName(candidate.getFullName())
                    .oldStatus(oldStatus)
                    .newStatus(application.getStatus())
                    .note(note)
                    .jobTitle(job.getTitle())
                    .companyName(company.getName())
                    .changedAt(LocalDateTime.now())
                    .build();

            kafkaTemplate.send(TOPIC, String.valueOf(application.getId()), event);
        }
        catch (Exception e) {
            System.out.println("Error in publishStatusChanged -----------------  "+e.getMessage());
        }

    }
}
