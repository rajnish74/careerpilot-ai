package com.rajnish.job.service.impl;

import com.rajnish.job.dto.request.CompanyRequest;
import com.rajnish.job.dto.response.CompanyResponse;
import com.rajnish.job.dto.response.SocialLinkResponse;
import com.rajnish.job.entity.Company;
import com.rajnish.job.entity.SocialLink;
import com.rajnish.job.enums.CompanyStatus;
import com.rajnish.job.enums.CompanyType;
import com.rajnish.job.enums.IndustryType;
import com.rajnish.job.mapper.CompanyMapper;
import com.rajnish.job.repository.CompanyRepository;
import com.rajnish.job.service.CompanyService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public CompanyResponse createCompany(Long ownerId, CompanyRequest request) throws Exception {
        if (companyRepository.existsByOwnerId(ownerId)) {
            throw new Exception("You already have a company registered. "+ "Only one company per user is allowed.");
        }
        if (companyRepository.existsByName(request.getName())) {
            throw new Exception("Company already exists. Please choose a different name.");
        }
        if (request.getRegistrationNumber() != null && companyRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new Exception("Company with this registration number already exists. Please provide a unique registration number.");
        }

        String slug = generateUniqueSlug(request.getName());

        Company company = Company.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .tagline(request.getTagline())
                .logoUrl(request.getLogoUrl())
                .coverImageUrl(request.getCoverImageUrl())
                .website(request.getWebsite())
                .foundedYear(request.getFoundedYear())
                .phone(request.getPhone())
                .email(request.getEmail())
                .companySize(request.getCompanySize())
                .companyType(request.getCompanyType())
                .industryType(request.getIndustryType())
                .ownerId(ownerId)
                .registrationNumber(request.getRegistrationNumber())
                .socialLinks(mapSocialLinks(request.getSocialLinks()))
                .build();

        Company savedCompany = companyRepository.save(company);

        return CompanyMapper.toResponse(savedCompany);
    }

    private List<SocialLink> mapSocialLinks(List<SocialLinkResponse> socialLinks) {
        if(socialLinks == null || socialLinks.isEmpty()) {
            return new ArrayList<SocialLink>();
        }
        return socialLinks.stream()
                .map(e->SocialLink.builder()
                        .platform(e.getPlatform())
                        .url(e.getUrl())
                        .build())
                .collect(Collectors.toList());
    }

    private String generateUniqueSlug(String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s-", "-");

        if(companyRepository.existsBySlug(base)) {
            return base;
        }
        int counter = 1;
        while(companyRepository.existsBySlug(base+"-"+counter)) {
            counter++;
        }
        return base+"-"+counter;

    }

    @Override
    public CompanyResponse getCompanyById(Long id) throws Exception {
        Company company = companyRepository.findById(id)
                .orElseThrow(()->new Exception("Company not found with id: "+id));

        return CompanyMapper.toResponse(company);
    }

    @Override
    public CompanyResponse getMyCompany(Long ownerId) throws Exception {
        Company company = companyRepository.findByOwnerId(ownerId)
                .orElseThrow(()->new Exception("No company found for the owner with id: "+ownerId));
        return CompanyMapper.toResponse(company);
    }

    @Override
    public List<CompanyResponse> getMyCompanies(CompanyType companyType,
                                                IndustryType industryType, CompanyStatus companyStatus) {

        return companyRepository.findByFilters(companyType, industryType, companyStatus)
                .stream()
                .map(CompanyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyResponse updateCompany(Long companyId, Long ownerId,
                                         CompanyRequest request) throws Exception {

        Company company = getCompanyEntityById(companyId);
        if(!company.getName().equals(request.getName()) && companyRepository.existsByName(request.getName())) {
            throw new Exception("Company with this name already exists. Please choose a different name.");
        }

        if (request.getRegistrationNumber() != null &&
                !request.getRegistrationNumber().equals(company.getRegistrationNumber())
                && companyRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new Exception("Company with this registration number already exists. Please provide a unique registration number.");
        }

        company.setName(request.getName());
        company.setDescription(request.getDescription());
        company.setTagline(request.getTagline());
        company.setLogoUrl(request.getLogoUrl());
        company.setCoverImageUrl(request.getCoverImageUrl());
        company.setWebsite(request.getWebsite());
        company.setFoundedYear(request.getFoundedYear());
        company.setPhone(request.getPhone());
        company.setEmail(request.getEmail());
        company.setCompanySize(request.getCompanySize());
        company.setCompanyType(request.getCompanyType());
        company.setIndustryType(request.getIndustryType());
        company.setRegistrationNumber(request.getRegistrationNumber());
        company.setSocialLinks(mapSocialLinks(request.getSocialLinks()));

        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    public CompanyResponse verifyCompany(Long companyId) throws Exception {
        Company company = getCompanyEntityById(companyId);
        company.setStatus(CompanyStatus.ACTIVE);
        company.setVerified(true);


        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    public void deleteCompany(Long companyId, Long ownerId) throws Exception {
        Company company = getCompanyEntityById(companyId);
        assertOwner(company, ownerId);
        companyRepository.delete(company);

    }

    private void assertOwner(Company company, Long ownerId) throws Exception {
        if (!company.getOwnerId().equals(ownerId)) {
            throw new Exception("You are not owner for this company");
        }
    }

    @Override
    public CompanyResponse deactivateCompany(Long companyId) throws Exception {

        Company company = getCompanyEntityById(companyId);
        company.setStatus(CompanyStatus.SUSPENDED);
        company.setVerified(false);


        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    public Company getCompanyEntityById(Long id) throws Exception {

        return companyRepository.findById(id)
                .orElseThrow(()->new Exception("Company not found with id: "+id));
    }
}
