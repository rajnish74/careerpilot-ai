package com.rajnish.job.repository;

import com.rajnish.job.dto.response.ApplicationNoteResponse;
import com.rajnish.job.entity.ApplicationNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationNoteRepository extends JpaRepository<ApplicationNote, Long> {

    List<ApplicationNote> findByApplicationId(Long applicationId);
}
