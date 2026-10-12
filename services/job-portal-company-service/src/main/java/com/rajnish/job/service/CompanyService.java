package com.rajnish.job.service;

import com.rajnish.job.dto.request.CompanyRequest;
import com.rajnish.job.dto.response.CompanyResponse;
import com.rajnish.job.entity.Company;
import com.rajnish.job.enums.CompanyStatus;
import com.rajnish.job.enums.CompanyType;
import com.rajnish.job.enums.IndustryType;

import java.util.List;

public interface CompanyService {

    CompanyResponse createCompany(Long ownerId, CompanyRequest request) throws Exception;
    CompanyResponse getCompanyById(Long id) throws Exception;
    CompanyResponse getMyCompany(Long ownerId) throws Exception;
    List<CompanyResponse> getMyCompanies(CompanyType companyType,
                                         IndustryType industryType,
                                         CompanyStatus companyStatus);

    CompanyResponse updateCompany(Long companyId, Long ownerId, CompanyRequest request) throws Exception;
    CompanyResponse verifyCompany(Long companyId) throws Exception;
    void deleteCompany(Long companyId, Long ownerId) throws Exception;
    CompanyResponse deactivateCompany(Long companyId) throws Exception;

//    INTERNAL SERVICE CALL
    Company getCompanyEntityById(Long id) throws Exception;
}
