package com.rajnish.job.controller;

import com.rajnish.job.dto.response.ApiResponse;
import com.rajnish.job.dto.response.ApplicationNoteResponse;
import com.rajnish.job.payload.AddApplicationNoteRequest;
import com.rajnish.job.service.ApplicationNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications/{applicationId}/notes")
public class ApplicationNoteController {

    private final ApplicationNoteService noteService;

    @PostMapping
    public ResponseEntity<ApplicationNoteResponse> addNote(
            @PathVariable Long applicationId,
            @RequestHeader("X-User-Id") Long employerId,
            @RequestBody @Valid AddApplicationNoteRequest req
    ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.addNote(applicationId, employerId, req));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationNoteResponse>> getNotes(@PathVariable Long applicationId,
                                                                  @RequestHeader("X-User-Id") Long employerId){
        return ResponseEntity.ok(noteService.getNotesByApplication(applicationId, employerId));

    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<ApiResponse> deleteNote(
            @PathVariable Long applicationId,
            @PathVariable Long noteId,
            @RequestHeader("X-User-Id") Long employerId
    ) throws Exception {
        noteService.deleteNote(applicationId, noteId, employerId);
        return ResponseEntity.ok(new ApiResponse("Note deleted successfully", true)
        );
    }
}
