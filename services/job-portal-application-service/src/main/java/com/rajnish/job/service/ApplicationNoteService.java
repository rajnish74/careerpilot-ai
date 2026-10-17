package com.rajnish.job.service;

import com.rajnish.job.dto.response.ApplicationNoteResponse;
import com.rajnish.job.payload.AddApplicationNoteRequest;

import java.util.List;

public interface ApplicationNoteService {

    ApplicationNoteResponse addNote(Long applicationId, Long employerId, AddApplicationNoteRequest request) throws Exception;

    List<ApplicationNoteResponse> getNotesByApplication(Long applicationId, Long employerId);

    void deleteNote(Long applicationId, Long noteId, Long employerId) throws Exception;
}
