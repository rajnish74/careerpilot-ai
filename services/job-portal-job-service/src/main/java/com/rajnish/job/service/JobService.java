package com.rajnish.job.service;

import com.rajnish.job.dto.request.JobRequest;
import com.rajnish.job.dto.response.JobResponse;
import com.rajnish.job.payload.JobSearchRequest;

import java.util.List;

public interface JobService {

    public JobResponse createJob(Long employerId, JobRequest request);

    public JobResponse getJobById(Long id) throws Exception;

    public List<JobResponse> getJobs(JobSearchRequest request);

    public List<JobResponse> getJobsByCompany(Long companyId);


    public JobResponse updateJob(Long jobId, Long employerId, JobRequest request) throws Exception;

    public JobResponse publishJob(Long jobId, Long employerId) throws Exception;

    public JobResponse closeJob(Long jobId, Long employerId) throws Exception;

    void deleteJob(Long jobId, Long employerId) throws Exception;

    List<JobResponse> getAllJobsAdmin();
}
