package com.rajnish.job.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddAwardRequest {

    @NotBlank(message = "Award title is required")
    private String title;

    @NotBlank(message = "Award issuer is required")
    private String issuer;

    private LocalDate awardDate;

    private String description;

    @Pattern(
            regexp = "^$|^https?://.*$",
            message = "Credential URL must be valid"
    )
    private String credentialUrl;

    private Integer displayOrder;
}
