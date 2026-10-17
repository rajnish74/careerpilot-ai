package com.rajnish.job.payload;

import com.rajnish.job.enums.ApplicationStatus;
import lombok.Data;

@Data
public class UpdateApplicationStatusRequest {

    private ApplicationStatus status;
}
