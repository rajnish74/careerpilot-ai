package com.rajnish.job.controller;

import com.rajnish.job.dto.response.ApiResponse;
import com.rajnish.job.dto.response.AwardResponse;
import com.rajnish.job.payload.AddAwardRequest;
import com.rajnish.job.service.AwardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/{resumeId}/awards")
public class AwardController {

    private final AwardService awardService;

    @PostMapping
    public ResponseEntity<AwardResponse> addAward(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddAwardRequest request
    ) throws Exception {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(awardService.addAward(
                        resumeId, candidateId, request));
    }

    @GetMapping
    public ResponseEntity<List<AwardResponse>> getAwards(
            @PathVariable Long resumeId
    ) throws Exception {

        return ResponseEntity.ok(
                awardService.getAwards(resumeId));
    }

    @PutMapping("/{awardId}")
    public ResponseEntity<AwardResponse> updateAward(
            @PathVariable Long resumeId,
            @PathVariable Long awardId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddAwardRequest request
    ) throws Exception {

        return ResponseEntity.ok(
                awardService.updateAward(
                        awardId, resumeId, candidateId, request));
    }

    @DeleteMapping("/{awardId}")
    public ResponseEntity<ApiResponse> deleteAward(
            @PathVariable Long resumeId,
            @PathVariable Long awardId,
            @RequestHeader("X-User-Id") Long candidateId
    ) throws Exception {

        awardService.deleteAward(
                awardId, resumeId, candidateId);

        return ResponseEntity.ok(
                new ApiResponse("Award deleted successfully", true));
    }
}
