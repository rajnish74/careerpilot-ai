package com.rajnish.job.mapper;

import com.rajnish.job.dto.response.SavedJobResponse;
import com.rajnish.job.entity.SavedJob;

public class PreferenceMapper {

    public static SavedJobResponse toSavedJobResponse(SavedJob savedJob) {

        return SavedJobResponse.builder()
                .id(savedJob.getId())
                .candidateId(savedJob.getCandidateId())
                .jobId(savedJob.getJobId())
                .savedAt(savedJob.getSavedAt())
                .build();
    }
}
