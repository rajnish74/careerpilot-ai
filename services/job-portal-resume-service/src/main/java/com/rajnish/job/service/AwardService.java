package com.rajnish.job.service;

import com.rajnish.job.dto.response.AwardResponse;
import com.rajnish.job.payload.AddAwardRequest;

import java.util.List;

public interface AwardService {

    AwardResponse addAward(Long resumeId, Long candidateId, AddAwardRequest request) throws Exception;

    List<AwardResponse> getAwards(Long resumeId) throws Exception;

    AwardResponse updateAward(
            Long awardId, Long resumeId, Long candidateId, AddAwardRequest request) throws Exception;

    void deleteAward(Long awardId, Long resumeId, Long candidateId) throws Exception;
}
