package com.rajnish.job.controller;

import com.rajnish.job.payload.*;
import com.rajnish.job.service.ResumeAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai/resume")
public class ResumeAiController {

    private final ResumeAiService resumeAiService;

    @PostMapping("/summary")
    public ResponseEntity<AiTextResponse> generateSummary(@RequestBody ResumeSummaryRequest resumeSummaryRequest) {
        AiTextResponse aiTextResponse = resumeAiService.generateProfessionalSummary(resumeSummaryRequest);
        return ResponseEntity.ok(aiTextResponse);
    }

    @PostMapping("/experience-bullets")
    public ResponseEntity<WorkExperienceBulletResponse> generateBullets(@RequestBody WorkExperienceBulletRequest resumeBulletRequest) {
        WorkExperienceBulletResponse response = resumeAiService.generateWorkExperienceBullet(resumeBulletRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/improvements")
    public ResponseEntity<ResumeImprovementResponse> getImprovements(@RequestBody ResumeImprovementRequest request) {
        ResumeImprovementResponse response = resumeAiService.generateResumeImprovement(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/career-feedback")
    public ResponseEntity<CareerFeedbackResponse> getCareerFeedback(@RequestBody CareerFeedbackRequest request) {
        CareerFeedbackResponse response = resumeAiService.generateCareerFeedback(request);
        return ResponseEntity.ok(response);
    }




}
