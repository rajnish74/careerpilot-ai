package com.rajnish.job.mapper;

import com.rajnish.job.dto.response.WorkExperienceResponse;
import com.rajnish.job.entity.WorkExperience;

public class WorkExperienceMapper {

    public static WorkExperienceResponse toWorkExperienceResponse(
            WorkExperience exp) {

        if (exp == null) return null;

        return WorkExperienceResponse.builder()
                .id(exp.getId())
                .companyName(exp.getCompanyName())
                .companyLogoUrl(exp.getCompanyLogoUrl())
                .jobTitle(exp.getJobTitle())
                .employmentType(exp.getEmploymentType())
                .location(exp.getLocation())
                .startDate(exp.getStartDate())
                .endDate(exp.getEndDate())
                .isCurrentJob(exp.getIsCurrentJob())
                .description(exp.getDescription())
                .technologies(exp.getTechnologies())
                .displayOrder(exp.getDisplayOrder())
                .build();
    }
}
