package com.rajnish.job.controller;

import com.rajnish.job.payload.JobAlertSuggestRequest;
import com.rajnish.job.payload.JobAlertSuggestResponse;
import com.rajnish.job.payload.SearchEnhanceResponse;
import com.rajnish.job.payload.SearchEnhancedRequest;
import com.rajnish.job.service.SearchAiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai/search")
public class AiSearchController {

    private final SearchAiService searchAIService;

    @PostMapping("/enhance")
    public ResponseEntity<SearchEnhanceResponse> enhanceSearch(
            @Valid @RequestBody SearchEnhancedRequest request
    ) throws Exception {
        return ResponseEntity.ok(searchAIService.enhanceSearch(request));
    }

    @PostMapping("/alert-suggestion")
    public ResponseEntity<JobAlertSuggestResponse> suggestAlertCriteria(
            @Valid @RequestBody JobAlertSuggestRequest request
    ) throws Exception {
        return ResponseEntity.ok(searchAIService.suggestJobAlertCriteria(request));
    }
}
