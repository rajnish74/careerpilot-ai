package com.rajnish.job.payload;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CareerFeedbackResponse {

    private int profileStrength;

    private List<String> shortlistingIssues;

    private List<Improvements> improvements;

    private List<JobTarget> targetJob;

    private String overallSummary;

    @Data
    public static class Improvements{
        private String area;
        private String issue;
        private String action;
        private String priority;
    }

    @Data
    public static class JobTarget{
        private String jobTitle;
        private String reason;
        private String skillMatch;
    }


}
