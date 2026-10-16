package com.rajnish.job.mapper;

import com.rajnish.job.dto.PersonalInfoResponse;
import com.rajnish.job.dto.response.*;
import com.rajnish.job.entity.*;

import java.util.List;

public class ResumeMapper {

    public static PersonalInfoResponse toPersonalInfoResponse(
            PersonalInfo personalInfo) {

        if (personalInfo == null) {return null;}

        return PersonalInfoResponse.builder()
                .firstName(personalInfo.getFirstName())
                .lastName(personalInfo.getLastName())
                .email(personalInfo.getEmail())
                .phone(personalInfo.getPhone())
                .headline(personalInfo.getHeadline())
                .country(personalInfo.getCountry())
                .city(personalInfo.getCity())
                .githubUrl(personalInfo.getGithubUrl())
                .linkedinUrl(personalInfo.getLinkedinUrl())
                .portfolioUrl(personalInfo.getPortfolioUrl())
                .websiteUrl(personalInfo.getWebsiteUrl())
                .build();
    }

    public static ResumeResponse toResponse(Resume resume,
                                            List<WorkExperienceResponse> workExperience,
                                             List<EducationResponse> education,
                                             List<ResumeSkillResponse> skills,
                                            List<ProjectResponse> projects,
                                            List<CertificationResponse> certifications,
                                             List<AwardResponse> awards,
                                            List<LanguageResponse> languages) {
        if (resume == null) { return null; }

        return ResumeResponse.builder()
                .id(resume.getId())
                .candidateId(resume.getCandidateId())
                .title(resume.getTitle())
                .template(resume.getTemplate())
                .visibility(resume.getVisibility())
                .isDefault(resume.getIsDefault())
                .personalInfo(ResumeMapper.toPersonalInfoResponse(resume.getPersonalInfo()))
                .summary(resume.getSummary())
                .completionScore(resume.getCompletionScore())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .workExperiences(workExperience)
                .educations(education)
                .skills(skills)
                .projects(projects)
                .languages(languages)
                .certifications(certifications)
                .awards(awards)
                .build();
    }

    public static ResumeSkillResponse toSkillResponse(ResumeSkills skill) {
        if (skill == null) return null;

        return ResumeSkillResponse.builder()
                .id(skill.getId())
                .skillName(skill.getSkillName())
                .proficiencyLevel(skill.getProficiencyLevel())
                .yearsOfExperience(skill.getYearsOfExperience())
                .displayOrder(skill.getDisplayOrder())
                .build();
    }

    public static EducationResponse toEducationResponse(Education edu) {
        if (edu == null) return null;

        return EducationResponse.builder()
                .id(edu.getId())
                .institutionName(edu.getInstitutionName())
                .degree(edu.getDegree())
                .fieldOfStudy(edu.getFieldOfStudy())
                .grade(edu.getGrade())
                .startDate(edu.getStartDate())
                .endDate(edu.getEndDate())
                .isCurrentlyStudying(edu.getIsCurrentlyStudying())
                .description(edu.getDescription())
                .displayOrder(edu.getDisplayOrder())
                .build();
    }

    public static ProjectResponse toProjectResponse(Projects project) {
        if (project == null) {
            return null;
        }

        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .technologies(project.getTechnologies())
                .projectUrl(project.getProjectUrl())
                .sourceCodeUrl(project.getSourceCodeUrl())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .isOngoing(project.getIsOngoing())
                .displayOrder(project.getDisplayOrder())
                .build();
    }

    public static LanguageResponse toLanguageResponse(Language language) {
        if (language == null) { return null; }
        return LanguageResponse.builder()
                .id(language.getId())
                .languageName(language.getLanguageName())
                .proficiency(language.getProficiency())
                .displayOrder(language.getDisplayOrder())
                .build();
    }


    public static AwardResponse toAwardResponse(Award award) {
        if (award == null) {
            return null;
        }

        return AwardResponse.builder()
                .id(award.getId())
                .resumeId(award.getResume().getId())
                .title(award.getTitle())
                .issuer(award.getIssuer())
                .awardDate(award.getAwardDate())
                .description(award.getDescription())
                .credentialUrl(award.getCredentialUrl())
                .displayOrder(award.getDisplayOrder())
                .build();
    }

    public static CertificationResponse toCertificationResponse(
            Certification certification) {

        if (certification == null) {
            return null;
        }

        return CertificationResponse.builder()
                .id(certification.getId())
                .resumeId(certification.getResume().getId())
                .name(certification.getName())
                .issuingOrganization(
                        certification.getIssuingOrganization())
                .issueDate(certification.getIssueDate())
                .expirationDate(certification.getExpirationDate())
                .credentialId(certification.getCredentialId())
                .credentialUrl(certification.getCredentialUrl())
                .noExpiration(certification.getNoExpiration())
                .displayOrder(certification.getDisplayOrder())
                .build();
    }

}
