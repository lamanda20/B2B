package com.b2b.service;

import com.b2b.dto.CompanyDto;

import java.util.List;

public interface CompanyService {
    List<CompanyDto> getAllCompanies();
    CompanyDto getCompanyById(Long id);
    CompanyDto createCompany(CompanyDto companyDto);
    CompanyDto updateCompany(Long id, CompanyDto companyDto);
    void deleteCompany(Long id);
    CompanyDto disableCompany(Long id);
    CompanyDto enableCompany(Long id) ;

    // Get a company by the account email (used for authenticated user's company)
    CompanyDto getCompanyByEmail(String email);
}
