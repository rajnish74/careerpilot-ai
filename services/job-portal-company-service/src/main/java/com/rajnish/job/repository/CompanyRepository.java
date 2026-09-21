package com.rajnish.job.repository;

import com.rajnish.job.entity.Company;
import com.rajnish.job.enums.CompanyStatus;
import com.rajnish.job.enums.CompanyType;
import com.rajnish.job.enums.IndustryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByOwnerId(Long ownerId);
    boolean existsByOwnerId(Long ownerId);
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    boolean existsByRegistrationNumber(String registrationNumber);

    @Query(
            "select c from Company c where " +
                    "(:companyType Is NULL OR c.companyType=:companyType) AND " +
                    "(:industryType Is NULL OR c.industryType=:industryType) AND " +
                    "(:status Is NULL OR c.status=:status)"

    )
    List<Company> findByFilters(@Param("companyType") CompanyType companyType,
                                @Param("industryType") IndustryType industryType,
                                @Param("status") CompanyStatus status);


}
