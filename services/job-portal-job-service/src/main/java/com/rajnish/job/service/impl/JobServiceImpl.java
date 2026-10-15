package com.rajnish.job.service.impl;

import com.rajnish.job.dto.request.JobRequest;
import com.rajnish.job.dto.response.CompanyResponse;
import com.rajnish.job.dto.response.JobResponse;
import com.rajnish.job.entity.Job;
import com.rajnish.job.entity.JobCategory;
import com.rajnish.job.entity.JobSkill;
import com.rajnish.job.entity.JobTag;
import com.rajnish.job.entity.embaddable.JobLocation;
import com.rajnish.job.entity.embaddable.SalaryRange;
import com.rajnish.job.enums.JobStatus;
import com.rajnish.job.mapper.JobMapper;
import com.rajnish.job.payload.JobSearchRequest;
import com.rajnish.job.repository.JobRepository;
import com.rajnish.job.repository.JobSpecification;
import com.rajnish.job.service.JobCategoryService;
import com.rajnish.job.service.JobService;
import com.rajnish.job.service.JobSkillService;
import com.rajnish.job.service.JobTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobCategoryService categoryService;
    private final JobSkillService skillService;
    private final JobTagService tagService;

    @Override
    public JobResponse createJob(Long employerId, JobRequest request) throws Exception {

        JobCategory category = categoryService.getCategoryEntityById(request.getCategoryId());

        Set<JobSkill> skills = request.getSkillIds() != null ?
                skillService.getSkillByIds(request.getSkillIds()) : Collections.emptySet();

        Set<JobTag> tags = request.getTagIds() != null ?
                tagService.getTagsByIds(request.getTagIds()) : Collections.emptySet();

        Long companyId = 1L;

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .requirements(request.getRequirements())
                .responsibilities(request.getResponsibilities())
                .benefits(request.getBenefits())
                .companyId(companyId)
                .employerId(employerId)
                .category(category)
                .skills(skills)
                .tags(tags)
                .location(buildLocation(request))
                .salaryRange(buildSalaryRange(request))
                .jobType(request.getJobType())
                .workMode(request.getWorkMode())
                .experienceLevel(request.getExperienceLevel())
                .openings(request.getOpenings() != null ? request.getOpenings() : 1)
                .applicationDeadline(request.getApplicationDeadline())
                .expiresAt(request.getExpiresAt())
                .active(true)
                .status(JobStatus.DRAFT)
                .build();

        Job savedJob = jobRepository.save(job);

        return convertToResponse(savedJob);
    }

    private JobResponse convertToResponse(Job savedJob){
        CompanyResponse companyResponse = CompanyResponse.builder()
                .build();

        return JobMapper.toResponse(savedJob, companyResponse);
    }

    private SalaryRange buildSalaryRange(JobRequest request) {
        return SalaryRange.builder()
                .minSalary(request.getMinSalary())
                .maxSalary(request.getMaxSalary())
                .build();
    }

    private JobLocation buildLocation(JobRequest request) {
        return JobLocation.builder()
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .zipCode(request.getZipCode())
                .build();
    }

    @Override
    public JobResponse getJobById(Long id) throws Exception {
        Job job = jobRepository.findById(id).orElseThrow(
                ()-> new Exception("Job not found"));
        return convertToResponse(job);
    }

    @Override
    public List<JobResponse> getJobs(JobSearchRequest request) {
        List<Job> jobs = jobRepository.findAll(JobSpecification.build(request));
        return jobs.stream().map(
                this::convertToResponse
        ).collect(Collectors.toList());
    }

    @Override
    public List<JobResponse> getJobsByCompany(Long companyId) {
        List<Job> jobs = jobRepository.findByCompanyId(companyId);
        return jobs.stream().map(
                this::convertToResponse
        ).collect(Collectors.toList());
    }

    @Override
    public JobResponse updateJob(Long jobId, Long employerId, JobRequest request) throws Exception {
        Job job = jobRepository.findById(jobId).orElseThrow(
                ()-> new Exception("Job not found"));
        assertEmployer(job, employerId);

        JobCategory category = categoryService.getCategoryEntityById(request.getCategoryId());

        Set<JobSkill> skills = request.getSkillIds() != null ?
                skillService.getSkillByIds(request.getSkillIds()) : Collections.emptySet();

        Set<JobTag> tags = request.getTagIds() != null ?
                tagService.getTagsByIds(request.getTagIds()) : Collections.emptySet();

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());
        job.setResponsibilities(request.getResponsibilities());
        job.setBenefits(request.getBenefits());
        job.setCategory(category);
        job.setSkills(skills);
        job.setTags(tags);
        job.setWorkMode(request.getWorkMode());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setOpenings(request.getOpenings() != null ? request.getOpenings() : job.getOpenings());
        job.setApplicationDeadline(request.getApplicationDeadline());
        job.setExpiresAt(request.getExpiresAt());
        job.setJobType(request.getJobType());
        job.setSalaryRange(buildSalaryRange(request));
        job.setLocation(buildLocation(request));

        return convertToResponse(jobRepository.save(job));
    }

    @Override
    public JobResponse publishJob(Long jobId, Long employerId) throws Exception {
        Job job = jobRepository.findById(jobId).orElseThrow(
                ()-> new Exception("Job not found"));
        assertEmployer(job, employerId);
        if (job.getStatus() == JobStatus.CLOSED || job.getStatus() == JobStatus.EXPIRED){
            throw new Exception("Job has been closed");
        }
        job.setStatus(JobStatus.OPEN);
        job.setPublishedAt(LocalDateTime.now());
        job.setActive(true);
        return convertToResponse(jobRepository.save(job));
    }

    private void assertEmployer(Job job, Long employerId) throws Exception {
        if (!job.getEmployerId().equals(employerId)) {
            throw new Exception("you are not the employer who posted this job");
        }
    }

    @Override
    public JobResponse closeJob(Long jobId, Long employerId) throws Exception {
        Job job = jobRepository.findById(jobId).orElseThrow(
                ()-> new Exception("Job not found"));
        assertEmployer(job, employerId);

        job.setStatus(JobStatus.CLOSED);
        job.setClosedAt(LocalDateTime.now());
        job.setActive(false);
        return convertToResponse(jobRepository.save(job));
    }

    @Override
    public void deleteJob(Long jobId, Long employerId) throws Exception {
        Job job = jobRepository.findById(jobId).orElseThrow(
                ()-> new Exception("Job not found"));
        assertEmployer(job, employerId);
        jobRepository.delete(job);
    }

    @Override
    public List<JobResponse> getAllJobsAdmin() {
        return jobRepository.findAll().stream().map(
                this::convertToResponse
        ).collect(Collectors.toList());
    }
}



