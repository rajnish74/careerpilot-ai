package com.rajnish.job.service;

import com.rajnish.job.dto.response.JobCategoryResponse;
import com.rajnish.job.entity.JobCategory;
import com.rajnish.job.payload.JobCategoryRequest;

import java.util.List;

public interface JobCategoryService {

    JobCategoryResponse createJobCategory(JobCategoryRequest request) throws Exception;
    List<JobCategoryResponse> getAllCategories();

    JobCategoryResponse getCategoryById(Long id) throws Exception;
    JobCategoryResponse updateJobCategory(Long id,JobCategoryRequest request) throws Exception;

    void deleteCategory(Long id) throws Exception;
    JobCategory getCategoryEntityById(Long id) throws Exception;
}
