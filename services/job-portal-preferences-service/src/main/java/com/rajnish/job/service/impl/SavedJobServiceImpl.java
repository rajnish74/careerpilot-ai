package com.rajnish.job.service.impl;

import com.rajnish.job.dto.response.SavedJobResponse;
import com.rajnish.job.entity.SavedJob;
import com.rajnish.job.mapper.PreferenceMapper;
import com.rajnish.job.payload.SavedJobRequest;
import com.rajnish.job.repository.SavedJobRepository;
import com.rajnish.job.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;


    @Override
    public SavedJobResponse saveJob(Long candidateId, SavedJobRequest request) throws Exception {
        if (isSaved(candidateId, request.getJobId())) {
            throw new Exception("job already saved");
        }
        SavedJob savedJob = SavedJob.builder()
                .candidateId(candidateId)
                .jobId(request.getJobId())
                .build();

        SavedJob savedJobSaved = savedJobRepository.save(savedJob);
        return PreferenceMapper.toSavedJobResponse(savedJobSaved);
    }

    @Override
    public void unsavedJob(Long candidateId, Long savedJobId) throws Exception {
        SavedJob savedJob = savedJobRepository.findById(savedJobId).orElseThrow(()->
                new Exception("job not found"));

        if (!savedJob.getCandidateId().equals(candidateId)) {
            throw new Exception("job not found");
        }
        savedJobRepository.delete(savedJob);
    }


    @Override
    public List<SavedJobResponse> getSavedJobs(Long candidateId) {
        return savedJobRepository.findByCandidateId(candidateId)
                .stream().map(PreferenceMapper::toSavedJobResponse).toList();
    }

    @Override
    public boolean isSaved(Long candidateId, Long jobId) {
        return savedJobRepository.existsByCandidateIdAndJobId(candidateId, jobId);
    }
}
