package com.rajnish.job.service.impl;

import com.rajnish.job.dto.response.AwardResponse;
import com.rajnish.job.entity.Award;
import com.rajnish.job.entity.Resume;
import com.rajnish.job.mapper.ResumeMapper;
import com.rajnish.job.payload.AddAwardRequest;
import com.rajnish.job.respository.AwardRepository;
import com.rajnish.job.service.AwardService;
import com.rajnish.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AwardServiceImpl implements AwardService {

    private final AwardRepository awardRepository;
    private final ResumeService resumeService;

    @Override
    public AwardResponse addAward(
            Long resumeId,
            Long candidateId,
            AddAwardRequest req) throws Exception {

        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        Award award = Award.builder()
                .resume(resume)
                .title(req.getTitle())
                .issuer(req.getIssuer())
                .awardDate(req.getAwardDate())
                .description(req.getDescription())
                .credentialUrl(req.getCredentialUrl())
                .displayOrder(
                        req.getDisplayOrder() != null
                                ? req.getDisplayOrder() : 0)
                .build();

        return ResumeMapper.toAwardResponse(
                awardRepository.save(award));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AwardResponse> getAwards(Long resumeId)
            throws Exception {

        resumeService.getResumeEntity(resumeId);

        return awardRepository
                .findByResume_IdOrderByDisplayOrderAscIdAsc(resumeId)
                .stream()
                .map(ResumeMapper::toAwardResponse)
                .toList();
    }

    @Override
    public AwardResponse updateAward(
            Long awardId,
            Long resumeId,
            Long candidateId,
            AddAwardRequest req) throws Exception {

        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        Award award = awardRepository.findById(awardId)
                .orElseThrow(() ->
                        new Exception("Award not found"));

        assertResume(award.getResume(), resumeId);

        award.setTitle(req.getTitle());
        award.setIssuer(req.getIssuer());
        award.setAwardDate(req.getAwardDate());
        award.setDescription(req.getDescription());
        award.setCredentialUrl(req.getCredentialUrl());

        if (req.getDisplayOrder() != null) {
            award.setDisplayOrder(req.getDisplayOrder());
        }

        return ResumeMapper.toAwardResponse(
                awardRepository.save(award));
    }

    @Override
    public void deleteAward(
            Long awardId,
            Long resumeId,
            Long candidateId) throws Exception {

        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        Award award = awardRepository.findById(awardId)
                .orElseThrow(() ->
                        new Exception("Award not found"));

        assertResume(award.getResume(), resumeId);
        awardRepository.delete(award);
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
            throw new Exception("Award does not belong to this resume");
        }
    }
}
