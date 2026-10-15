package com.rajnish.job.controller;

import com.rajnish.job.dto.response.ApiResponse;
import com.rajnish.job.dto.response.JobTagResponse;
import com.rajnish.job.payload.JobTagRequest;
import com.rajnish.job.repository.JobTagRepository;
import com.rajnish.job.service.JobTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/job-tags")
public class JobTagController {

    private final JobTagService jobTagService;

    @PostMapping
    public ResponseEntity<JobTagResponse> createJobTag(@RequestBody @Valid
                                                           JobTagRequest jobTagRequest) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobTagService.createTag(jobTagRequest));
    }

    @GetMapping
    public ResponseEntity<List<JobTagResponse>> getAllJobTags() throws Exception {
        return ResponseEntity.ok(jobTagService.getAllTags());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobTagResponse> getTagById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(jobTagService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobTagResponse> updateTag(@PathVariable Long id,
                                                    @RequestBody @Valid
                                                     JobTagRequest jobTagRequest) throws Exception {
        return ResponseEntity.ok(jobTagService.updateTag(id, jobTagRequest));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTag(@PathVariable Long id) throws Exception {
        jobTagService.deleteTag(id);
        return ResponseEntity.ok(new ApiResponse("Tag deleted successfully.", true));
    }

}
