package com.rajnish.job.payload;

import com.rajnish.job.enums.ResumeTemplate;
import com.rajnish.job.enums.ResumeVisibility;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateResumeRequest {

    @NotBlank(message = "Resume title is required")
    private String title;

    private ResumeTemplate template;

    private ResumeVisibility visibility;

    private Boolean isDefault;


}
