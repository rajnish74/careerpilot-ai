package com.rajnish.job.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiTextResponse {

    private String content;

    @Builder.Default
    private LocalDateTime generatedAt = LocalDateTime.now();
}
