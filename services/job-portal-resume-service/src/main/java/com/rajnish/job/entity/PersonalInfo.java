package com.rajnish.job.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalInfo {

    private String firstName;
    private String lastName;

    private String email;
    private String phone;
    private String headline;
    private String city;
    private String country;

    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String websiteUrl;
}
