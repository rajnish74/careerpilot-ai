package com.rajnish.job.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class JobDescriptionRequest {

    @NotBlank(message = "job title is required")
    private String title;

    private List<String> skills;
    private String experienceLevel;
    private String jobType;
    private String workMode;
    private String category;
    private String additionalContext;
}
