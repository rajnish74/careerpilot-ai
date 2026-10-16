package com.rajnish.job.service.impl;

import com.rajnish.job.dto.response.LanguageResponse;
import com.rajnish.job.entity.Language;
import com.rajnish.job.entity.Resume;
import com.rajnish.job.mapper.ResumeMapper;
import com.rajnish.job.payload.AddLanguageRequest;
import com.rajnish.job.respository.LanguageRepository;
import com.rajnish.job.service.LanguageService;
import com.rajnish.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final ResumeService resumeService;

    @Override
    public LanguageResponse addLanguage(Long resumeId, Long candidateId, AddLanguageRequest request) throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);

        assertOwner(resume, candidateId);
        Language language = Language.builder()
                .resume(resume)
                .languageName(request.getLanguageName())
                .proficiency(request.getProficiency())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();
        Language savedLanguage = languageRepository.save(language);
        return ResumeMapper.toLanguageResponse(savedLanguage);
    }

    @Override
    public List<LanguageResponse> getLanguages(Long resumeId) {
        return languageRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream().map(ResumeMapper::toLanguageResponse).toList();
    }

    @Override
    public LanguageResponse updateLanguage(Long languageId, Long resumeId, Long candidateId, AddLanguageRequest request) throws Exception {
        Language language = languageRepository.findById(languageId).orElseThrow(()->
                new Exception("Language not found"));

        assertOwner(language.getResume(), candidateId);
        language.setLanguageName(request.getLanguageName());
        language.setProficiency(request.getProficiency());
        if (request.getDisplayOrder() != null) {language.setDisplayOrder(request.getDisplayOrder());}
        return ResumeMapper.toLanguageResponse(languageRepository.save(language));
    }

    @Override
    public void deleteLanguage(Long languageId, Long resumeId, Long candidateId) throws Exception {
        Language language = languageRepository.findById(languageId).orElseThrow(()->
                new Exception("Language not found"));

        assertOwner(language.getResume(), candidateId);
        languageRepository.delete(language);
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if(!resume.getCandidateId().equals(candidateId)) {
            throw new Exception("resume not found");
        }
    }
}
