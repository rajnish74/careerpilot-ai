package com.rajnish.job.service;

import com.rajnish.job.dto.request.CompanyRequest;
import com.rajnish.job.dto.response.CompanyResponse;
import com.rajnish.job.entity.Company;
import com.rajnish.job.enums.CompanySize;
import com.rajnish.job.enums.CompanyStatus;
import com.rajnish.job.enums.CompanyType;
import com.rajnish.job.enums.IndustryType;

import java.util.List;

public interface CompanyService {

    CompanyResponse createCompany(Long ownerId, CompanyRequest request);
    CompanyResponse getCompanyById(Long id);
    CompanyResponse getMyCompany(Long ownerId);
    List<CompanyResponse> getMyCompanies(CompanyType companyType,
                                         IndustryType industryType,
                                         CompanyStatus companyStatus);

    CompanyResponse updateCompany(Long companyId, Long ownerId, CompanyRequest request);
    CompanyResponse verifyCompany(Long companyId);
    void deleteCompany(Long companyId);
    CompanyResponse deactivateCompany(Long companyId);

//    INTERNAL SERVICE CALL
    Company getCompanyEntityById(Long id);
}
