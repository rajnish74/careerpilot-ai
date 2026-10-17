package com.rajnish.job.controller;

import com.rajnish.job.dto.response.ApiResponse;
import com.rajnish.job.dto.response.SavedJobResponse;
import com.rajnish.job.payload.SavedJobRequest;
import com.rajnish.job.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/preferences/saved-jobs")
public class SavedJobController {

    private final SavedJobService savedJobService;

    @PostMapping
    public ResponseEntity<SavedJobResponse> savedJob(@RequestHeader("X-User-Id") Long candidateId,
                                                     @RequestBody SavedJobRequest savedJobRequest) throws Exception {
        return ResponseEntity.ok(savedJobService.saveJob(candidateId, savedJobRequest));
    }

    @GetMapping
    public ResponseEntity<List<SavedJobResponse>> getMySavedJobs(@RequestHeader("X-User-Id") Long candidateId){
        return ResponseEntity.ok(savedJobService.getSavedJobs(candidateId));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isSaved(@RequestHeader("X-User-Id") Long candidateId,
                                           @RequestParam Long jobId){
        return ResponseEntity.ok(savedJobService.isSaved(candidateId, jobId));
    }

    @DeleteMapping("/{savedJobId}")
    public ResponseEntity<ApiResponse> unsavedJob(@PathVariable Long savedJobId,
                                                  @RequestHeader("X-User-Id") Long candidateId) throws Exception {
        savedJobService.unsavedJob(savedJobId, candidateId);
        return ResponseEntity.ok(new ApiResponse("job removed from saved list", true));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("SavedJobController is working");
    }
}
