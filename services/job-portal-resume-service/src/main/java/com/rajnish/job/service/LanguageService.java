package com.rajnish.job.service;

import com.rajnish.job.dto.response.LanguageResponse;
import com.rajnish.job.payload.AddLanguageRequest;

import java.util.List;

public interface LanguageService {

    LanguageResponse addLanguage(Long resumeId, Long candidateId, AddLanguageRequest request) throws Exception;

    List<LanguageResponse> getLanguages(Long resumeId);
    LanguageResponse updateLanguage(Long languageId, Long resumeId, Long candidateId, AddLanguageRequest request) throws Exception;
    void deleteLanguage(Long languageId, Long resumeId, Long candidateId) throws Exception;
}
