package com.rajnish.job.controller;

import com.rajnish.job.dto.response.ApiResponse;
import com.rajnish.job.dto.response.CertificationResponse;
import com.rajnish.job.payload.AddCertificationRequest;
import com.rajnish.job.service.CertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/{resumeId}/certifications")
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping
    public ResponseEntity<CertificationResponse> addCertification(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddCertificationRequest request
    ) throws Exception {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(certificationService.addCertification(
                        resumeId, candidateId, request));
    }

    @GetMapping
    public ResponseEntity<List<CertificationResponse>>
    getCertifications(
            @PathVariable Long resumeId
    ) throws Exception {

        return ResponseEntity.ok(
                certificationService.getCertifications(resumeId));
    }

    @PutMapping("/{certificationId}")
    public ResponseEntity<CertificationResponse>
    updateCertification(
            @PathVariable Long resumeId,
            @PathVariable Long certificationId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddCertificationRequest request
    ) throws Exception {

        return ResponseEntity.ok(
                certificationService.updateCertification(
                        certificationId,
                        resumeId,
                        candidateId,
                        request));
    }

    @DeleteMapping("/{certificationId}")
    public ResponseEntity<ApiResponse> deleteCertification(
            @PathVariable Long resumeId,
            @PathVariable Long certificationId,
            @RequestHeader("X-User-Id") Long candidateId
    ) throws Exception {

        certificationService.deleteCertification(
                certificationId, resumeId, candidateId);

        return ResponseEntity.ok(
                new ApiResponse(
                        "Certification deleted successfully", true));
    }
}
