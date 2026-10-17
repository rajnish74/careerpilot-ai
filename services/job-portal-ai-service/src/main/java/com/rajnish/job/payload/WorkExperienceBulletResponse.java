package com.rajnish.job.payload;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WorkExperienceBulletResponse {

    private List<String> bullets;
}
