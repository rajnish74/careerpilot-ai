package com.rajnish.job.service;

import com.rajnish.job.dto.response.ApplicationResponse;
import com.rajnish.job.entity.Application;
import com.rajnish.job.enums.ApplicationStatus;
import com.rajnish.job.payload.CompanyApplicationFilterRequest;
import com.rajnish.job.payload.CreateApplicationRequest;
import com.rajnish.job.payload.WithdrawApplicationRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ApplicationService {

    ApplicationResponse createApplication(Long candidateId, CreateApplicationRequest request) throws Exception;
    ApplicationResponse getApplicationById(Long id) throws Exception;

    List<ApplicationResponse> getMyApplications(Long candidateId);

    List<ApplicationResponse> getApplicationsForJob(Long jobId);

    List<ApplicationResponse> getApplicationsForCompany(Long userId, CompanyApplicationFilterRequest filter);

    ApplicationResponse updateStatus(Long applicationId, Long employerId, ApplicationStatus status) throws Exception;

    ApplicationResponse withdraw(Long applicationId, Long candidateId, WithdrawApplicationRequest request) throws Exception;


    ApplicationResponse toggleStar(Long applicationId, Long employerId) throws Exception;
    void deleteApplication(Long applicationId, Long candidateId) throws Exception;


    Application getApplicationEntity(Long id) throws Exception;

}
