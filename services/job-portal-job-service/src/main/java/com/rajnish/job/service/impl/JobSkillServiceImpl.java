package com.rajnish.job.service.impl;

import com.rajnish.job.dto.response.JobSkillResponse;
import com.rajnish.job.entity.JobSkill;
import com.rajnish.job.mapper.JobSkillMapper;
import com.rajnish.job.payload.JobSkillRequest;
import com.rajnish.job.repository.JobSkillRepository;
import com.rajnish.job.service.JobSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobSkillServiceImpl implements JobSkillService {

    private final JobSkillRepository jobSkillRepository;

    @Override
    public JobSkillResponse createSkill(JobSkillRequest request) throws Exception {
        if (jobSkillRepository.existsByName(request.getName())) {
            throw new Exception("skill name is already exists.");
        }

        String slug = generateUniqueSlug(request.getName());
        JobSkill skill = JobSkill.builder()
                .name(request.getName())
                .slug(slug)
                .category(request.getCategory())
                .build();

        JobSkill savedSkill = jobSkillRepository.save(skill);
        return JobSkillMapper.toJobSkillResponse(savedSkill);
    }

    @Override
    public List<JobSkillResponse> getAllSkills() {
        return jobSkillRepository.findByActiveTrue().stream()
                .map(JobSkillMapper::toJobSkillResponse)
                .collect(Collectors.toList());
    }

    @Override
    public JobSkillResponse getSkillById(Long id) throws Exception {
        JobSkill skill = jobSkillRepository.findById(id).orElseThrow(()->
                new Exception("job skill not found"));
        return JobSkillMapper.toJobSkillResponse(skill);
    }

    @Override
    public JobSkillResponse updateSkill(Long id, JobSkillRequest request) throws Exception {
        JobSkill skill = jobSkillRepository.findById(id).orElseThrow(()->
                new Exception("job skill not found"));

        if(!skill.getName().equals(request.getName()) && jobSkillRepository.existsByName(request.getName())) {
            throw new Exception("skill name is already exists.");
        }
        skill.setName(request.getName());
        skill.setCategory(request.getCategory());

        JobSkill savedSkill = jobSkillRepository.save(skill);
        return JobSkillMapper.toJobSkillResponse(savedSkill);
    }

    @Override
    public void deleteSkill(Long id) throws Exception {
        JobSkill skill = jobSkillRepository.findById(id).orElseThrow(()->
                new Exception("job skill not found"));
        skill.setActive(false);
        jobSkillRepository.save(skill);
    }

    @Override
    public Set<JobSkill> getSkillByIds(Set<Long> ids) {
        Set<JobSkill> skills = new HashSet<>(jobSkillRepository.findAllById(ids));
        return skills;
    }

    private String generateUniqueSlug(String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s-]", "")
                .trim()
                .replaceAll("[\\s-]+", "-");

        if(jobSkillRepository.existsBySlug(base)) {
            return base;
        }
        int counter = 1;
        while(jobSkillRepository.existsBySlug(base+"-"+counter)) {
            counter++;
        }
        return base+"-"+counter;

    }
}
