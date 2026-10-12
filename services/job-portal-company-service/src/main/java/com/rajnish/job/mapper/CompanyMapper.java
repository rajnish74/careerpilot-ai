package com.rajnish.job.mapper;

import com.rajnish.job.dto.response.CompanyResponse;
import com.rajnish.job.dto.response.SocialLinkResponse;
import com.rajnish.job.entity.Company;
import com.rajnish.job.entity.SocialLink;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyMapper {

    public static SocialLinkResponse toSocialLinkResponse(SocialLink socialLink) {
        return SocialLinkResponse.builder()
                .platform(socialLink.getPlatform())
                .url(socialLink.getUrl())
                .build();
    }

    public static CompanyResponse toResponse(Company company) {

        List<SocialLinkResponse> socialLinks = company.getSocialLinks() == null ? Collections.emptyList() :
                company.getSocialLinks().stream()
                        .map(CompanyMapper::toSocialLinkResponse)
                        .toList();

        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .slug(company.getSlug())
                .description(company.getDescription())
                .tagline(company.getTagline())
                .logoUrl(company.getLogoUrl())
                .coverImageUrl(company.getCoverImageUrl())
                .website(company.getWebsite())
                .foundedYear(company.getFoundedYear())
                .phone(company.getPhone())
                .email(company.getEmail())
                .companySize(company.getCompanySize())
                .companyType(company.getCompanyType())
                .industryType(company.getIndustryType())
                .status(company.getStatus())
                .active(company.getActive())
                .ownerId(company.getOwnerId())
                .socialLinks(socialLinks)

                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }
}
