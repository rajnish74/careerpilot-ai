package com.rajnish.job.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationNoteResponse {

    private Long id;
    private Long addedByUserId;
    private String content;
    private LocalDateTime createdAt;
}
