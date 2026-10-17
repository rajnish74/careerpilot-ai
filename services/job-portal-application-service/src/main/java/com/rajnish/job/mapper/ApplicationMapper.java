package com.rajnish.job.mapper;

import com.rajnish.job.dto.response.*;
import com.rajnish.job.entity.Application;
import com.rajnish.job.entity.ApplicationNote;
import com.rajnish.job.enums.ApplicationStatus;
import com.rajnish.job.payload.CreateApplicationRequest;

import java.util.List;

public class ApplicationMapper {

    public static Application toEntity(CreateApplicationRequest req,
                                       Long candidateId, Long companyId, Long employerId) {
        if (req == null) {
            return null;
        }

        return Application.builder()
                .candidateId(candidateId)
                .jobId(req.getJobId())
                .companyId(companyId)
                .employerId(employerId)
                .resumeId(req.getResumeId())
                .coverLetter(req.getCoverLetter())
                .expectedSalary(req.getExpectedSalary())
                .availableFrom(req.getAvailableFrom())
                .status(ApplicationStatus.PENDING)
                .build();
    }

    public static ApplicationResponse toResponse(Application application,
                                                 List<ApplicationNote> notes,
                                                 JobResponse job,
                                                 CompanyResponse company,
                                                 UserResponse candidate) {

        return ApplicationResponse.builder()
                .id(application.getId())
                .candidate(candidate)
                .employerId(application.getEmployerId())
                .job(job)
                .company(company)
                .status(application.getStatus())
                .resumeId(application.getResumeId())
                .coverLetter(application.getCoverLetter())
                .expectedSalary(application.getExpectedSalary())
                .availableFrom(application.getAvailableFrom())
                .isStarted(application.getIsStarted())

                .notes(notes.stream().map(ApplicationMapper::toNoteResponse).toList())

                .withdrawnAt(application.getWithdrawnAt())
                .withdrawnReason(application.getWithdrawnReason())
                .appliedAt(application.getAppliedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }

    public static ApplicationNoteResponse toNoteResponse(ApplicationNote note){
        return ApplicationNoteResponse.builder()
                .id(note.getId())
                .addedByUserId(note.getAddedByUserId())
                .content(note.getContent())
                .createdAt(note.getCreatedAtt())
                .build();
    }
}
