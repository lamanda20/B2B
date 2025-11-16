package com.b2b.service.impl;

import com.b2b.model.Company;
import com.b2b.repository.CompanyRepository;
import com.b2b.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public AccountServiceImpl(CompanyRepository companyRepository, PasswordEncoder encoder) {
        this.companyRepository = companyRepository;
        this.encoder = encoder;
    }

    @Override
    public void changePassword(String email, String currentRaw, String newRaw) {
        Company company = companyRepository.findByEmailIgnoreCase(email).orElseThrow();

        if (!encoder.matches(currentRaw, company.getPassword()))
            throw new IllegalArgumentException("Invalid current password");

        if (encoder.matches(newRaw, company.getPassword()))
            throw new IllegalArgumentException("New password must be different");

        company.setPassword(encoder.encode(newRaw));
        company.setMustChangePassword(false);
        companyRepository.save(company);
    }
}