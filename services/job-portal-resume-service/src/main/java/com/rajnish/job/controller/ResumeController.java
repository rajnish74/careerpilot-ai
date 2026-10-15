package com.rajnish.job.controller;

import com.rajnish.job.dto.PersonalInfoResponse;
import com.rajnish.job.dto.response.ApiResponse;
import com.rajnish.job.dto.response.ResumeResponse;
import com.rajnish.job.payload.CreateResumeRequest;
import com.rajnish.job.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<ResumeResponse> createResume(@RequestHeader("X-User-Id") Long candidateId,
                                                       @RequestBody @Valid CreateResumeRequest request) {
        return ResponseEntity.ok(resumeService.createResume(candidateId, request));
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<ResumeResponse> getResumeById(@PathVariable Long resumeId,
                                                        @RequestHeader("X-User-Id") Long candidateId) throws Exception {
        return ResponseEntity.ok(resumeService.getResumeById(resumeId, candidateId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ResumeResponse>> getMyResume(@RequestHeader("X-User-Id") Long candidateId){
        return ResponseEntity.ok(resumeService.getMyResumes(candidateId));
    }

    @PutMapping("/{resumeId}/personal-info")
    public ResponseEntity<ResumeResponse> updatePersonalInfo(@PathVariable Long resumeId,
                                                             @RequestHeader("X-User-Id") Long candidateId,
                                                             @RequestBody @Valid PersonalInfoResponse response) throws Exception {
        return ResponseEntity.ok(resumeService.updatePersonalInfo(resumeId, candidateId, response));
    }

    @PatchMapping("/{resumeId}/summary")
    public ResponseEntity<ResumeResponse> updateSummary(@PathVariable Long resumeId,
                                                        @RequestHeader("X-User-Id") Long candidateId,
                                                        @RequestParam String summary) throws Exception {
        return ResponseEntity.ok(resumeService.updateSummary(resumeId, candidateId, summary));
    }

    @PatchMapping("/{resumeId}/set-default")
    public ResponseEntity<ResumeResponse> setDefaultResume(@PathVariable Long resumeId,
                                                           @RequestHeader("X-User-Id") Long candidateId) throws Exception {
        return ResponseEntity.ok(resumeService.setDefaultResume(resumeId, candidateId));
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<ApiResponse> deleteResume(@PathVariable Long resumeId,
                                                    @RequestHeader("X-User-Id") Long candidateId) throws Exception {
        resumeService.deleteResume(resumeId, candidateId);
        return ResponseEntity.ok(new ApiResponse("Resume deleted successfully.", true));
    }


}
