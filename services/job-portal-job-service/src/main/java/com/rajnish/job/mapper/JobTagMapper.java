package com.rajnish.job.mapper;

import com.rajnish.job.dto.response.JobTagResponse;
import com.rajnish.job.entity.JobTag;

public class JobTagMapper {
    public static JobTagResponse toTagResponse(JobTag jobTag) {
        return JobTagResponse.builder()
                .id(jobTag.getId())
                .slug(jobTag.getSlug())
                .name(jobTag.getName())
                .build();
    }
}
