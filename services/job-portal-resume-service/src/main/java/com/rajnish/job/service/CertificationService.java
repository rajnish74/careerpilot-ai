package com.rajnish.job.service;


import com.rajnish.job.dto.response.CertificationResponse;
import com.rajnish.job.payload.AddCertificationRequest;

import java.util.List;

public interface CertificationService {

    CertificationResponse addCertification(
            Long resumeId,
            Long candidateId,
            AddCertificationRequest request
    ) throws Exception;

    List<CertificationResponse> getCertifications(Long resumeId)
            throws Exception;

    CertificationResponse updateCertification(
            Long certificationId,
            Long resumeId,
            Long candidateId,
            AddCertificationRequest request
    ) throws Exception;

    void deleteCertification(
            Long certificationId,
            Long resumeId,
            Long candidateId
    ) throws Exception;
}
