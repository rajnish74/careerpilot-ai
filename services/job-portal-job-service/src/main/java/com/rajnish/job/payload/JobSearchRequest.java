package com.rajnish.job.payload;

import com.rajnish.job.enums.ExperienceLevel;
import com.rajnish.job.enums.JobStatus;
import com.rajnish.job.enums.JobType;
import com.rajnish.job.enums.WorkMode;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobSearchRequest {

    private String keyword;

    private Long categoryId;

    private List<Long> skillIds;

    private List<Long> tagIds;

    private Long companyId;

    private String location;

    private BigDecimal minSalary;
    private BigDecimal maxSalary;

    private JobStatus status;
    private JobType jobType;

    private WorkMode workMode;
    private ExperienceLevel experienceLevel;

    private Integer minOpenings;
    private Integer maxOpenings;
}
