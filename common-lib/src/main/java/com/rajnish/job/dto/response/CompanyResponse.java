package com.rajnish.job.dto.response;

import com.rajnish.job.enums.CompanySize;
import com.rajnish.job.enums.CompanyStatus;
import com.rajnish.job.enums.CompanyType;
import com.rajnish.job.enums.IndustryType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyResponse {

    private Long id;
    private String name;
    private String slug;
    private String description;

    private String tagline;
    private String logoUrl;
    private String coverImageUrl;
    private String website;
    private String phone;
    private String email;
    private Integer foundedYear;

    private CompanySize companySize;
    private CompanyType companyType;
    private IndustryType industryType;
    private CompanyStatus companyStatus;
    private Boolean active;


    private Long ownerId;


    private List<SocialLinkResponse> socialLinks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime verifiedAt;
}
