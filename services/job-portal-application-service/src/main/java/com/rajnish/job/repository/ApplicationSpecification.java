package com.rajnish.job.repository;

import com.rajnish.job.entity.Application;
import com.rajnish.job.enums.AiShortlistStatus;
import com.rajnish.job.enums.ApplicationStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ApplicationSpecification {

    public static Specification<Application> forCompanyWithFilters(
            Long companyId,
            Long jobId,
            ApplicationStatus status,
            boolean isStarred,
            AiShortlistStatus aiShortlistStatus,
            Integer minAiScore
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("companyId"), companyId));

            if (jobId != null) {
                predicates.add(cb.equal(root.get("jobId"), jobId));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (isStarred) predicates.add(cb.equal(root.get("isStarred"), isStarred));

            if (aiShortlistStatus != null) {
                predicates.add(cb.equal(root.get("aiShortlistStatus"), aiShortlistStatus));
            }

            if (minAiScore != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.<Integer>get("aiScore"), minAiScore));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
