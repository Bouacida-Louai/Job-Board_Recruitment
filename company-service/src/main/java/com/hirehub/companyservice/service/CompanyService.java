package com.hirehub.companyservice.service;


import com.hirehub.companyservice.dto.CompanyRequest;
import com.hirehub.companyservice.dto.CompanyResponse;
import com.hirehub.companyservice.entity.Company;
import com.hirehub.companyservice.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyResponse createCompany(Long userId, CompanyRequest request) {
        if (companyRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException(
                    "Company profile already exists for this account");
        }

        Company company = Company.builder()
                .userId(userId)
                .name(request.getName())
                .description(request.getDescription())
                .industry(request.getIndustry())
                .website(request.getWebsite())
                .location(request.getLocation())
                .build();

        return toResponse(companyRepository.save(company));
    }

    @Cacheable(value = "companies", key = "#id")
    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return toResponse(company);
    }

    @Cacheable(value = "companies", key = "'user_' + #userId")
    public CompanyResponse getCompanyByUserId(Long userId) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return toResponse(company);
    }

    @CacheEvict(value = "companies", allEntries = true)
    public CompanyResponse updateCompany(Long userId, CompanyRequest request) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setName(request.getName());
        company.setDescription(request.getDescription());
        company.setIndustry(request.getIndustry());
        company.setWebsite(request.getWebsite());
        company.setLocation(request.getLocation());

        return toResponse(companyRepository.save(company));
    }

    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // This method will be called by job-service via REST
    // to check if a company exists before posting a job
    public boolean existsById(Long id) {
        return companyRepository.existsById(id);
    }

    private CompanyResponse toResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .userId(company.getUserId())
                .name(company.getName())
                .description(company.getDescription())
                .industry(company.getIndustry())
                .website(company.getWebsite())
                .location(company.getLocation())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }
}