package com.rajnish.job.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkExperienceBulletRequest {

    @NotBlank(message = "job title is required")
    private String jobTitle;

    private String company;

    @NotBlank(message = "Raw description is required")
    private String rawDescriptions;

    private String achievementsHint;
}
