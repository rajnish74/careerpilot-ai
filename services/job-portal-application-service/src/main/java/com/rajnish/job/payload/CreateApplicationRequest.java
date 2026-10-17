package com.rajnish.job.payload;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateApplicationRequest {

    @NotNull(message = "Job id is required")
    private Long jobId;

    @NotNull(message = "Resume id is required")
    private Long resumeId;

    @Size(max = 3000, message = "Cover letter message not exceeded 3000 characters")
    private String coverLetter;

    @DecimalMin(value = "0.0", message = "Expected salary must not be negative")
    private BigDecimal expectedSalary;

    private LocalDate availableFrom;
}
