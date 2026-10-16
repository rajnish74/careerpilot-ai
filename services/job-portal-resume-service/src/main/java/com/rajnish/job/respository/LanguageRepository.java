package com.rajnish.job.respository;

import com.rajnish.job.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    List<Language> findByResume_IdOrderByDisplayOrderAsc(Long resumeId);
}
