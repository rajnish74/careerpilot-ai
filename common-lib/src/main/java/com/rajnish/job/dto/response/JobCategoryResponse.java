package com.rajnish.job.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private String slug;
    private String iconUrl;
    private Boolean active;

    private Long parentId;
    private String parentName;


    private List<JobCategoryResponse> subCategories;
    private LocalDateTime createdAt;
}
