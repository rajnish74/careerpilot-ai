package com.rajnish.job.service.impl;

import com.rajnish.job.dto.response.ApplicationNoteResponse;
import com.rajnish.job.entity.Application;
import com.rajnish.job.entity.ApplicationNote;
import com.rajnish.job.mapper.ApplicationMapper;
import com.rajnish.job.payload.AddApplicationNoteRequest;
import com.rajnish.job.repository.ApplicationNoteRepository;
import com.rajnish.job.service.ApplicationNoteService;
import com.rajnish.job.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ApplicationNoteServiceImpl implements ApplicationNoteService {

    private final ApplicationNoteRepository applicationNoteRepository;
    private final ApplicationService applicationService;

    @Override
    public ApplicationNoteResponse addNote(Long applicationId, Long employerId, AddApplicationNoteRequest request) throws Exception {
        Application application = applicationService.getApplicationEntity(applicationId);
        assertEmployer(application, employerId);

        ApplicationNote applicationNote = ApplicationNote.builder()
                .application(application)
                .addedByUserId(employerId)
                .content(request.getContent())
                .build();

        ApplicationNote savedNote = applicationNoteRepository.save(applicationNote);

        return ApplicationMapper.toNoteResponse(savedNote);
    }

    @Override
    public List<ApplicationNoteResponse> getNotesByApplication(Long applicationId, Long employerId) {
        return applicationNoteRepository.findByApplicationId(applicationId)
                .stream().map(ApplicationMapper::toNoteResponse).toList();
    }

    @Override
    public void deleteNote(Long applicationId, Long noteId, Long employerId) throws Exception {
        Application application = applicationService.getApplicationEntity(applicationId);
        assertEmployer(application, employerId);

        ApplicationNote note = applicationNoteRepository.findById(noteId).orElseThrow(()->
                new Exception("Note with id " + noteId + " not found"));

        applicationNoteRepository.delete(note);
    }

    private void assertEmployer(Application application, Long employerId) throws Exception {
        if(!application.getEmployerId().equals(employerId)) {
            throw new Exception("you are not the employer for this application");
        }
    }
}
