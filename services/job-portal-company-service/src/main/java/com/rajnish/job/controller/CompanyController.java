package com.rajnish.job.controller;

import com.rajnish.job.dto.request.CompanyRequest;
import com.rajnish.job.dto.response.ApiResponse;
import com.rajnish.job.dto.response.CompanyResponse;
import com.rajnish.job.enums.CompanyStatus;
import com.rajnish.job.enums.CompanyType;
import com.rajnish.job.enums.IndustryType;
import com.rajnish.job.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@RequestHeader("X-User-id") Long ownerId,
                                                         @RequestBody @Valid CompanyRequest companyRequest) throws Exception {
       return ResponseEntity.status(HttpStatus.CREATED)
                .body(companyService.createCompany(ownerId, companyRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(companyService.getCompanyById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<CompanyResponse> getMyCompany(@RequestHeader("X-User-id") Long ownerId) throws Exception {
        return ResponseEntity.ok(companyService.getMyCompany(ownerId));
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies(
            @RequestParam(required = false)CompanyType companyType,
            @RequestParam(required = false) CompanyStatus status,
            @RequestParam(required = false)IndustryType industryType) throws Exception {
        return ResponseEntity.ok(companyService.getAllCompanies(companyType, industryType, status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@RequestHeader("X-User-id") Long ownerId,
                                                         @PathVariable Long id,
                                                         @RequestBody @Valid CompanyRequest companyRequest) throws Exception {
        return ResponseEntity.ok(companyService.updateCompany(ownerId, id, companyRequest));
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<CompanyResponse> verifyCompany(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(companyService.verifyCompany(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CompanyResponse> deactivateCompany(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(companyService.deactivateCompany(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCompany(@PathVariable Long id,
                                                     @RequestHeader("X-User-id") Long ownerId) throws Exception {

        companyService.deleteCompany(ownerId, id);
        return ResponseEntity.ok(new ApiResponse("Company deleted successfully", true));
    }






}
