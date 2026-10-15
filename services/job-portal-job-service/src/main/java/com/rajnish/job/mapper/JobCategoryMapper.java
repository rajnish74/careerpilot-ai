package com.rajnish.job.mapper;

import com.rajnish.job.dto.response.JobCategoryResponse;
import com.rajnish.job.entity.JobCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JobCategoryMapper {

    public static JobCategoryResponse toJobCategoryResponse(JobCategory category,
                                                            boolean includeChildren) {
        List<JobCategoryResponse> subCategories = null;
        if (includeChildren && category.getSubcategories() != null) {
            subCategories = category.getSubcategories()
                    .stream().map(sub->toJobCategoryResponse(sub,false))
                    .toList();
        }


        return JobCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .iconUrl(category.getIconUrl())
                .active(category.getActive())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .subCategories(subCategories)
                .createdAt(category.getCreatedAt())
                .build();
    }
}
