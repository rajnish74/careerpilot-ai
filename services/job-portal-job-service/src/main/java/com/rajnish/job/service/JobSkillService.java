package com.rajnish.job.service;

import com.rajnish.job.dto.response.JobSkillResponse;
import com.rajnish.job.entity.JobSkill;
import com.rajnish.job.payload.JobSkillRequest;

import java.util.List;
import java.util.Set;

public interface JobSkillService {

    JobSkillResponse createSkill(JobSkillRequest request) throws Exception;

    List<JobSkillResponse> getAllSkills();

    JobSkillResponse getSkillById(Long id) throws Exception;

    JobSkillResponse updateSkill(Long id,JobSkillRequest request) throws Exception;

    void deleteSkill(Long id) throws Exception;

    Set<JobSkill> getSkillByIds(Set<Long> ids);


}
