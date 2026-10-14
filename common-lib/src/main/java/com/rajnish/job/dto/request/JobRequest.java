package com.rajnish.job.dto.request;

import com.rajnish.job.enums.ExperienceLevel;
import com.rajnish.job.enums.JobType;
import com.rajnish.job.enums.WorkMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    private String requirements;
    private String responsibilities;
    private String benefits;

    @NotNull(message = "Company ID is required")
    private Long categoryId;

    private Set<Long> skillIds;

    private Set<Long> tagIds;

    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    @DecimalMin(value = "0.0", inclusive = true, message = "min salary must not be negative")
    private BigDecimal minSalary;

    @DecimalMin(value = "0.0", inclusive = true, message = "max salary must not be negative")
    private BigDecimal maxSalary;

    @NotNull(message = "Job type is required")
    private JobType jobType;

    @NotNull(message = "Experience level is required")
    private ExperienceLevel experienceLevel;

    @NotNull(message = "Work mode is required")
    private WorkMode workMode;

    @Min(value = 1, message = "Openings must be 1")
    private Integer openings =1;

    private LocalDate applicationDeadline;

    private LocalDate expiresAt;

}
