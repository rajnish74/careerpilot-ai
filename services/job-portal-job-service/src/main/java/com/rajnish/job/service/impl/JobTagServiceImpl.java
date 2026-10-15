package com.rajnish.job.service.impl;

import com.rajnish.job.dto.response.JobTagResponse;
import com.rajnish.job.entity.JobTag;
import com.rajnish.job.mapper.JobTagMapper;
import com.rajnish.job.payload.JobTagRequest;
import com.rajnish.job.repository.JobTagRepository;
import com.rajnish.job.service.JobTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobTagServiceImpl implements JobTagService {

    private final JobTagRepository jobTagRepository;

    @Override
    public JobTagResponse createTag(JobTagRequest request) throws Exception {
        if (jobTagRepository.existsByName(request.getName())) {
            throw new Exception("tag name is already exists.");
        }
        String slug = generateUniqueSlug(request.getName());

        JobTag tag = JobTag.builder()
                .slug(slug)
                .name(request.getName())
                .build();
        JobTag saved = jobTagRepository.save(tag);
        return JobTagMapper.toTagResponse(saved);
    }

    @Override
    public List<JobTagResponse> getAllTags() {
        return jobTagRepository.findAll().stream()
                .map(JobTagMapper::toTagResponse)
                .collect(Collectors.toList());
    }

    @Override
    public JobTagResponse getById(Long id) throws Exception {
        JobTag jobtag = getTagEntityById(id);
        return JobTagMapper.toTagResponse(jobtag);
    }

    @Override
    public JobTagResponse updateTag(Long id, JobTagRequest request) throws Exception {
        JobTag jobtag = getTagEntityById(id);

        if(!jobtag.getName().equals(request.getName()) && jobTagRepository.existsByName(request.getName())) {
            throw new Exception("tag name already exists");
        }
        jobtag.setName(request.getName());
        return JobTagMapper.toTagResponse(jobTagRepository.save(jobtag));
    }

    @Override
    public void deleteTag(Long id) throws Exception {
        JobTag jobtag = getTagEntityById(id);
        jobTagRepository.delete(jobtag);
    }

    @Override
    public JobTag getTagEntityById(Long id) throws Exception {
        return jobTagRepository.findById(id)
                .orElseThrow(()->new Exception("job tag not found"));
    }

    @Override
    public Set<JobTag> getTagsByIds(Set<Long> ids) throws Exception {
        List<JobTag> jobTags = jobTagRepository.findAllById(ids);
        return new HashSet<>(jobTags);
    }

    private String generateUniqueSlug(String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s-]", "")
                .trim()
                .replaceAll("[\\s-]+", "-");

        if(jobTagRepository.existsBySlug(base)) {
            return base;
        }
        int counter = 1;
        while(jobTagRepository.existsBySlug(base+"-"+counter)) {
            counter++;
        }
        return base+"-"+counter;

    }
}
