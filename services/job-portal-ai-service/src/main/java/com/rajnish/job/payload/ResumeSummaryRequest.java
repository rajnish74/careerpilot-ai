package com.rajnish.job.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeSummaryRequest {

    private String targetJobTitle;
    private List<WorkExperienceInfo> workExperiences;
    private List<String> skills;
    private List<EducationInfo> educations;
    private Integer yearsOfExperience;



    @Data
    public static class WorkExperienceInfo{
        private String jobTitle;
        private String company;
        private String description;

    }
    @Data

    public static class EducationInfo{
        private String degree;
        private String filedOfStudy;
        private String institutionName;
    }

}
