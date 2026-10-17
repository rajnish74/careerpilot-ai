package com.rajnish.job.payload;

import com.rajnish.job.enums.AiShortlistStatus;
import com.rajnish.job.enums.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyApplicationFilterRequest {

    private Long jobId;

    private ApplicationStatus status;

    private Boolean isStarted;

    private AiShortlistStatus aiShortlistStatus;

    private Integer minAiScore;

    private String sortBy;
}
