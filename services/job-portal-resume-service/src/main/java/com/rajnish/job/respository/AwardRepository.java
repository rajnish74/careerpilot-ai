package com.rajnish.job.respository;

import com.rajnish.job.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AwardRepository
        extends JpaRepository<Award, Long> {

    List<Award> findByResume_IdOrderByDisplayOrderAscIdAsc(
            Long resumeId
    );
}
