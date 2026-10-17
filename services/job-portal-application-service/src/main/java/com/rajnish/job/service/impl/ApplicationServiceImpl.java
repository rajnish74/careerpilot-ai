package com.rajnish.job.service.impl;

import com.rajnish.job.dto.response.ApplicationResponse;
import com.rajnish.job.dto.response.CompanyResponse;
import com.rajnish.job.dto.response.JobResponse;
import com.rajnish.job.dto.response.UserResponse;
import com.rajnish.job.entity.Application;
import com.rajnish.job.entity.ApplicationNote;
import com.rajnish.job.enums.ApplicationStatus;
import com.rajnish.job.mapper.ApplicationMapper;
import com.rajnish.job.payload.CompanyApplicationFilterRequest;
import com.rajnish.job.payload.CreateApplicationRequest;
import com.rajnish.job.payload.WithdrawApplicationRequest;
import com.rajnish.job.repository.ApplicationNoteRepository;
import com.rajnish.job.repository.ApplicationRepository;
import com.rajnish.job.repository.ApplicationSpecification;
import com.rajnish.job.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationNoteRepository applicationNoteRepository;

    @Override
    public ApplicationResponse createApplication(Long candidateId, CreateApplicationRequest request) throws Exception {
        if (applicationRepository.existsByCandidateIdAndJobId(candidateId, request.getJobId())) {
            throw new Exception("you have already applied");
        }
        Long companyId = 1L;
        Long employerId = 1L;

        Application application = ApplicationMapper.toEntity(request, candidateId, companyId, employerId);
        Application savedApplication = applicationRepository.save(application);
        return buildFullResponse(savedApplication);
    }

    @Override
    public ApplicationResponse getApplicationById(Long id) throws Exception {
        Application application = getApplicationEntity(id);
        return buildFullResponse(application);
    }

    @Override
    public List<ApplicationResponse> getMyApplications(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId)
                .stream().map(this::buildFullResponse).toList();
    }

    @Override
    public List<ApplicationResponse> getApplicationsForJob(Long jobId) {
        return applicationRepository.findByJobId(jobId)
                .stream().map(this::buildFullResponse).toList();
    }

    @Override
    public List<ApplicationResponse> getApplicationsForCompany(Long userId,
                                                               CompanyApplicationFilterRequest filter) {
        Long companyId = 1L;
        Sort sort = buildSort(filter.getSortBy());
         return applicationRepository.findAll(ApplicationSpecification.forCompanyWithFilters(
                 companyId,
                 filter.getJobId(),
                 filter.getStatus(),
                 filter.getIsStarted(),
                 filter.getAiShortlistStatus(),
                 filter.getMinAiScore()
                ), sort)
                .stream().map(this::buildFullResponse).toList();

    }



    @Override
    public ApplicationResponse updateStatus(Long applicationId, Long employerId, ApplicationStatus status) throws Exception {
        Application application = getApplicationEntity(applicationId);
        assertEmployer(application, employerId);
        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new Exception("candidate have already withdrawn");
        }
        application.setStatus(status);
        Application savedApplication = applicationRepository.save(application);
        return buildFullResponse(savedApplication);
    }



    @Override
    public ApplicationResponse withdraw(Long applicationId, Long candidateId, WithdrawApplicationRequest request) throws Exception {
        Application application = getApplicationEntity(applicationId);
        assertCandidate(application, candidateId);
        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setWithdrawnReason(request.getReason());
        Application savedApplication = applicationRepository.save(application);
        return buildFullResponse(savedApplication);
    }



    @Override
    public ApplicationResponse toggleStar(Long applicationId, Long employerId) throws Exception {
        Application application = getApplicationEntity(applicationId);
        assertEmployer(application, employerId);
        application.setIsStarted(!application.getIsStarted());

        Application savedApplication = applicationRepository.save(application);
        return buildFullResponse(savedApplication);
    }

    @Override
    public void deleteApplication(Long applicationId, Long candidateId) throws Exception {
        Application application = getApplicationEntity(applicationId);
        assertCandidate(application, candidateId);
        applicationRepository.delete(application);
    }

    @Override
    public Application getApplicationEntity(Long id) throws Exception {
        return applicationRepository.findById(id).orElseThrow(()->
                new Exception("application not found"));
    }

    public ApplicationResponse buildFullResponse(Application application) {
        JobResponse job = JobResponse.builder().id(application.getId()).build();
        CompanyResponse company = CompanyResponse.builder().id(application.getCompanyId()).build();
        UserResponse candidate = UserResponse.builder().id(application.getCandidateId()).build();

        List<ApplicationNote> notes = applicationNoteRepository.findByApplicationId(application.getId());
        return ApplicationMapper.toResponse(application,notes, job, company, candidate);
    }

    private Sort buildSort(String sortBy) {
        if ("AI_SCORE_DESC".equals(sortBy)) {
            return Sort.by(Sort.Order.desc("aiScore").with(Sort.NullHandling.NULLS_LAST));
        }
        else if ("AI_SCORE_ASC".equals(sortBy)) {
            return Sort.by(Sort.Order.asc("aiScore").with(Sort.NullHandling.NULLS_LAST));
        }
        return Sort.by(Sort.Direction.DESC, "appliedAt");
    }

    private void assertEmployer(Application application, Long employerId) throws Exception {
        if(!application.getEmployerId().equals(employerId)) {
            throw new Exception("you are not the employer for this application");
        }
    }

    private void assertCandidate(Application application, Long candidateId) throws Exception {
        if (!application.getCandidateId().equals(candidateId)) {
            throw new Exception("you are not owner of this application");
        }
    }
}
