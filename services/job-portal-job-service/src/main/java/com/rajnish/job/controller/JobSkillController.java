package com.rajnish.job.controller;

import com.rajnish.job.dto.response.ApiResponse;
import com.rajnish.job.dto.response.JobSkillResponse;
import com.rajnish.job.payload.JobSkillRequest;
import com.rajnish.job.repository.JobSkillRepository;
import com.rajnish.job.service.JobSkillService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/job-skills")
public class JobSkillController {

    private final JobSkillService jobSkillService;

    @PostMapping
    public ResponseEntity<JobSkillResponse> createSkill(@RequestBody @Valid
                                                        JobSkillRequest request) throws Exception{
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobSkillService.createSkill(request));
    }

    @GetMapping
    public ResponseEntity<List<JobSkillResponse>> getAllSkills() throws Exception{
        return ResponseEntity.ok(jobSkillService.getAllSkills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobSkillResponse> getSkillById(@PathVariable Long id) throws Exception{
        return ResponseEntity.ok(jobSkillService.getSkillById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobSkillResponse> updateSkill(@PathVariable Long id,
                                                        @RequestBody @Valid JobSkillRequest request) throws Exception{
        return ResponseEntity.ok(jobSkillService.updateSkill(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteJob(@PathVariable Long id) throws Exception {
        jobSkillService.deleteSkill(id);
        return ResponseEntity.ok(new ApiResponse("Skill deleted successfully", true));
    }


}
