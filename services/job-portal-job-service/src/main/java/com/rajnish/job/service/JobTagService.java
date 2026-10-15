package com.rajnish.job.service;

import com.rajnish.job.dto.response.JobTagResponse;
import com.rajnish.job.entity.JobTag;
import com.rajnish.job.payload.JobTagRequest;

import java.util.List;
import java.util.Set;

public interface JobTagService {

    JobTagResponse createTag(JobTagRequest request) throws Exception;
    List<JobTagResponse> getAllTags();
    JobTagResponse getById(Long id) throws Exception;
    JobTagResponse updateTag(Long id, JobTagRequest request) throws Exception;
    void deleteTag(Long id) throws Exception;
    JobTag getTagEntityById(Long id) throws Exception;
    Set<JobTag> getTagsByIds(Set<Long> ids) throws Exception;
}
