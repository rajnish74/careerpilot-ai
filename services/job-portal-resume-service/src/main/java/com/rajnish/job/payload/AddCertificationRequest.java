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
public class AddCertificationRequest {

    @NotBlank(message = "Certification name is required")
    private String name;

    @NotBlank(message = "Issuing organization is required")
    private String issuingOrganization;

    private LocalDate issueDate;

    private LocalDate expirationDate;

    private String credentialId;

    @Pattern(
            regexp = "^$|^https?://.*$",
            message = "Credential URL must be valid"
    )
    private String credentialUrl;

    @Builder.Default
    private Boolean noExpiration = false;

    private Integer displayOrder;
}
