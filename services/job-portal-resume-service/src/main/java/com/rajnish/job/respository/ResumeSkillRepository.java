package com.rajnish.job.respository;

import com.rajnish.job.entity.ResumeSkills;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeSkillRepository extends JpaRepository<ResumeSkills, Long> {

    List<ResumeSkills> findByResume_IdOrderByDisplayOrderAsc(Long resumeId);
}
