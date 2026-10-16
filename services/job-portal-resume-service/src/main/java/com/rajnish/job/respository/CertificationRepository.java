package com.rajnish.job.respository;

import com.rajnish.job.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificationRepository
        extends JpaRepository<Certification, Long> {

    List<Certification>
    findByResume_IdOrderByDisplayOrderAscIdAsc(Long resumeId);
}
