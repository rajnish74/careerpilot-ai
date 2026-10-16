package com.rajnish.job.service.impl;

import com.rajnish.job.dto.response.ResumeSkillResponse;
import com.rajnish.job.entity.Resume;
import com.rajnish.job.entity.ResumeSkills;
import com.rajnish.job.mapper.ResumeMapper;
import com.rajnish.job.payload.AddResumeSkillRequest;
import com.rajnish.job.respository.ResumeSkillRepository;
import com.rajnish.job.service.ResumeService;
import com.rajnish.job.service.ResumeSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeSkillServiceImpl implements ResumeSkillService {

    private final ResumeSkillRepository resumeSkillRepository;
    private final ResumeService resumeService;

    @Override
    public ResumeSkillResponse addSkill(Long resumeId, Long candidateId, AddResumeSkillRequest req) throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);

        assertOwner(resume, candidateId);
        ResumeSkills skills = ResumeSkills.builder()
                .resume(resume)
                .skillName(req.getSkillName())
                .proficiencyLevel(req.getProficiencyLevel())
                .yearsOfExperience(req.getYearsOfExperience())
                .displayOrder(req.getDisplayOrder() != null ? req.getDisplayOrder() : 0)
                .build();

        ResumeSkills savedSkill = resumeSkillRepository.save(skills);
        return ResumeMapper.toSkillResponse(savedSkill);
    }

    @Override
    public List<ResumeSkillResponse> getSkills(Long resumeId) {
        return resumeSkillRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream().map(ResumeMapper::toSkillResponse).toList();
    }

    @Override
    public ResumeSkillResponse updateSkill(Long skillId, Long resumeId, Long candidateId, AddResumeSkillRequest req) throws Exception {
        ResumeSkills skills = resumeSkillRepository.findById(skillId).orElseThrow(()->
                new Exception("Skill not found"));
        assertOwner(skills.getResume(), candidateId);
        skills.setSkillName(req.getSkillName());
        skills.setProficiencyLevel(req.getProficiencyLevel());
        skills.setYearsOfExperience(req.getYearsOfExperience());
        if (req.getDisplayOrder() != null) {
            skills.setDisplayOrder(req.getDisplayOrder());
        }
        return ResumeMapper.toSkillResponse(resumeSkillRepository.save(skills));
    }

    @Override
    public void deleteSkill(Long skillId, Long resumeId, Long candidateId) throws Exception {
        ResumeSkills skills = resumeSkillRepository.findById(skillId).orElseThrow(()->
                new Exception("Skill not found"));
        assertOwner(skills.getResume(), candidateId);
        resumeSkillRepository.delete(skills);
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if(!resume.getCandidateId().equals(candidateId)) {
            throw new Exception("resume not found");
        }
    }
}
