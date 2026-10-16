package com.rajnish.job.respository;

import com.rajnish.job.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Projects, Long> {

    List<Projects> findByResume_IdOrderByDisplayOrderAsc(Long resumeId);

}
