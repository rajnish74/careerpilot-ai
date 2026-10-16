package com.rajnish.job.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AwardResponse {

    private Long id;
    private Long resumeId;
    private String title;
    private String issuer;
    private LocalDate awardDate;
    private String description;
    private String credentialUrl;
    private Integer displayOrder;
}
