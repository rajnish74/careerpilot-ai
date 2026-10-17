package com.rajnish.job.dto.response;

import com.rajnish.job.enums.AiShortlistStatus;
import com.rajnish.job.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponse {

    private Long id;
    private UserResponse candidate;

    private JobResponse job;
    private CompanyResponse company;


    private ApplicationStatus status;

    private Long employerId;
    private Long resumeId;
    private String coverLetter;

    private BigDecimal expectedSalary;
    private LocalDate availableFrom;

    private Boolean isStarted;

    private List<ApplicationNoteResponse> notes;


    private LocalDateTime withdrawnAt;
    private String withdrawnReason;


    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;



}
