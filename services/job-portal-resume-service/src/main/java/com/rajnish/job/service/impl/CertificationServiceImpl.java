package com.rajnish.job.service.impl;

import com.rajnish.job.dto.response.CertificationResponse;
import com.rajnish.job.entity.Certification;
import com.rajnish.job.entity.Resume;
import com.rajnish.job.mapper.ResumeMapper;
import com.rajnish.job.payload.AddCertificationRequest;
import com.rajnish.job.respository.CertificationRepository;
import com.rajnish.job.service.CertificationService;
import com.rajnish.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CertificationServiceImpl
        implements CertificationService {

    private final CertificationRepository certificationRepository;
    private final ResumeService resumeService;

    @Override
    public CertificationResponse addCertification(
            Long resumeId,
            Long candidateId,
            AddCertificationRequest req) throws Exception {

        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        Certification certification = Certification.builder()
                .resume(resume)
                .name(req.getName())
                .issuingOrganization(req.getIssuingOrganization())
                .issueDate(req.getIssueDate())
                .expirationDate(req.getExpirationDate())
                .credentialId(req.getCredentialId())
                .credentialUrl(req.getCredentialUrl())
                .noExpiration(Boolean.TRUE.equals(req.getNoExpiration()))
                .displayOrder(
                        req.getDisplayOrder() != null
                                ? req.getDisplayOrder() : 0)
                .build();

        validateDates(certification);

        return ResumeMapper.toCertificationResponse(
                certificationRepository.save(certification));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificationResponse> getCertifications(
            Long resumeId) throws Exception {

        resumeService.getResumeEntity(resumeId);

        return certificationRepository
                .findByResume_IdOrderByDisplayOrderAscIdAsc(resumeId)
                .stream()
                .map(ResumeMapper::toCertificationResponse)
                .toList();
    }

    @Override
    public CertificationResponse updateCertification(
            Long certificationId,
            Long resumeId,
            Long candidateId,
            AddCertificationRequest req) throws Exception {

        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        Certification certification =
                certificationRepository.findById(certificationId)
                        .orElseThrow(() ->
                                new Exception("Certification not found"));

        assertResume(certification.getResume(), resumeId);

        certification.setName(req.getName());
        certification.setIssuingOrganization(
                req.getIssuingOrganization());
        certification.setIssueDate(req.getIssueDate());
        certification.setExpirationDate(req.getExpirationDate());
        certification.setCredentialId(req.getCredentialId());
        certification.setCredentialUrl(req.getCredentialUrl());
        certification.setNoExpiration(
                Boolean.TRUE.equals(req.getNoExpiration()));

        if (req.getDisplayOrder() != null) {
            certification.setDisplayOrder(req.getDisplayOrder());
        }

        validateDates(certification);

        return ResumeMapper.toCertificationResponse(
                certificationRepository.save(certification));
    }

    @Override
    public void deleteCertification(
            Long certificationId,
            Long resumeId,
            Long candidateId) throws Exception {

        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        Certification certification =
                certificationRepository.findById(certificationId)
                        .orElseThrow(() ->
                                new Exception("Certification not found"));

        assertResume(certification.getResume(), resumeId);
        certificationRepository.delete(certification);
    }

    private void assertOwner(Resume resume, Long candidateId)
            throws Exception {

        if (resume.getCandidateId() == null
                || !resume.getCandidateId().equals(candidateId)) {
            throw new Exception("Resume not found");
        }
    }

    private void assertResume(Resume resume, Long resumeId)
            throws Exception {

        if (resume.getId() == null
                || !resume.getId().equals(resumeId)) {
            throw new Exception(
                    "Certification does not belong to this resume");
        }
    }

    private void validateDates(Certification certification)
            throws Exception {

        if (Boolean.TRUE.equals(certification.getNoExpiration())) {
            certification.setExpirationDate(null);
        } else if (certification.getIssueDate() != null
                && certification.getExpirationDate() != null
                && certification.getExpirationDate()
                .isBefore(certification.getIssueDate())) {
            throw new Exception(
                    "Expiration date cannot be before issue date");
        }
    }
}
