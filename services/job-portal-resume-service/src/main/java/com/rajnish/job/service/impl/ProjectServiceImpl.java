package com.rajnish.job.service.impl;

import com.rajnish.job.dto.response.ProjectResponse;
import com.rajnish.job.entity.Projects;
import com.rajnish.job.entity.Resume;
import com.rajnish.job.mapper.ResumeMapper;
import com.rajnish.job.payload.AddProjectRequest;
import com.rajnish.job.respository.ProjectRepository;
import com.rajnish.job.service.ProjectService;
import com.rajnish.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ResumeService resumeService;

    @Override
    public ProjectResponse addProject(Long resumeId, Long candidateId, AddProjectRequest req) throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);

        assertOwner(resume, candidateId);
        Projects project = Projects.builder()
                .resume(resume)
                .title(req.getTitle())
                .description(req.getDescription())
                .technologies(req.getTechnologies() != null ? req.getTechnologies() : List.of())
                .projectUrl(req.getProjectUrl())
                .sourceCodeUrl(req.getSourceCodeUrl())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .isOngoing(Boolean.TRUE.equals(req.getIsOngoing()))
                .displayOrder(req.getDisplayOrder() != null ? req.getDisplayOrder() : 0)
                .build();

        Projects savedProject = projectRepository.save(project);

        return ResumeMapper.toProjectResponse(savedProject);
    }

    @Override
    public List<ProjectResponse> getAllProjects(Long resumeId) {
        return projectRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream().map(ResumeMapper::toProjectResponse).toList();
    }

    @Override
    public ProjectResponse updateProject(Long projectId, Long resumeId, Long candidateId, AddProjectRequest req) throws Exception {
        Projects projects = projectRepository.findById(projectId).orElseThrow(()->
                new Exception("projects not found"));

        assertOwner(projects.getResume(), candidateId);
        projects.setTitle(req.getTitle());
        projects.setDescription(req.getDescription());
        if (req.getTechnologies() != null) {projects.setTechnologies(req.getTechnologies());}
        projects.setProjectUrl(req.getProjectUrl());
        projects.setSourceCodeUrl(req.getSourceCodeUrl());
        projects.setStartDate(req.getStartDate());
        projects.setEndDate(req.getEndDate());
        projects.setIsOngoing(Boolean.TRUE.equals(req.getIsOngoing()));
        if (req.getDisplayOrder() != null) {projects.setDisplayOrder(req.getDisplayOrder());}
        return ResumeMapper.toProjectResponse(projectRepository.save(projects));
    }

    @Override
    public void deleteProject(Long projectId, Long resumeId, Long candidateId) throws Exception {
        Projects projects = projectRepository.findById(projectId).orElseThrow(()->
                new Exception("projects not found"));

        assertOwner(projects.getResume(), candidateId);
        projectRepository.delete(projects);
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if(!resume.getCandidateId().equals(candidateId)) {
            throw new Exception("resume not found");
        }
    }
}
