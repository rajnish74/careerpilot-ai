package com.rajnish.job.service;

import com.rajnish.job.dto.response.SavedJobResponse;
import com.rajnish.job.payload.SavedJobRequest;

import java.util.List;

public interface SavedJobService {

    SavedJobResponse saveJob(Long candidateId, SavedJobRequest request) throws Exception;
    void unsavedJob(Long candidateId, Long savedJobId) throws Exception;
    List<SavedJobResponse> getSavedJobs(Long candidateId);
    boolean isSaved(Long candidateId, Long jobId);
}
