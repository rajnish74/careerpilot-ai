package com.rajnish.job.controller;

import com.rajnish.job.dto.response.ApiResponse;
import com.rajnish.job.dto.response.LanguageResponse;
import com.rajnish.job.entity.Language;
import com.rajnish.job.payload.AddLanguageRequest;
import com.rajnish.job.service.LanguageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/{resumeId}/languages")
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    public ResponseEntity<LanguageResponse> addLanguage(@PathVariable Long resumeId,
                                                        @RequestHeader("X-User-Id") Long candidateId,
                                                        @RequestBody @Valid AddLanguageRequest request) throws Exception {
        return ResponseEntity.ok(languageService.addLanguage(resumeId, candidateId, request));
    }

    @GetMapping
    public ResponseEntity<List<LanguageResponse>> getLanguages(@PathVariable Long resumeId
    ) throws Exception {
        return ResponseEntity.ok(languageService.getLanguages(resumeId));
    }

    @PutMapping("/{languageId}")
    public ResponseEntity<LanguageResponse> updateLanguage(
            @PathVariable Long resumeId,
            @PathVariable Long languageId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddLanguageRequest addLanguageRequest
    ) throws Exception {
        return ResponseEntity.ok(
                languageService.updateLanguage(languageId, resumeId, candidateId, addLanguageRequest));
    }

    @DeleteMapping("/{languageId}")
    public ResponseEntity<ApiResponse> deleteLanguage(
            @PathVariable Long resumeId,
            @PathVariable Long languageId,
            @RequestHeader("X-User-Id") Long candidateId
    ) throws Exception {
        languageService.deleteLanguage(languageId, resumeId, candidateId);
        return ResponseEntity.ok(new ApiResponse("Language deleted successfully", true));
    }
}
