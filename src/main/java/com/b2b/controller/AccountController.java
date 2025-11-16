package com.b2b.controller;

import com.b2b.dto.PasswordChangeRequest;
import com.b2b.dto.CompanyDto;
import com.b2b.service.AccountService;
import com.b2b.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService account;
    private final CompanyService companyService;
    public AccountController(AccountService account, CompanyService companyService) { this.account = account; this.companyService = companyService; }

    @PutMapping("/password")
    public void changeMyPassword(@Valid @RequestBody PasswordChangeRequest req,
                                 Authentication auth) {
        String email = auth.getName(); // httpBasic → email du user connecté
        account.changePassword(email, req.getCurrentPassword(), req.getNewPassword());
    }

    @GetMapping("/my-company")
    public ResponseEntity<CompanyDto> getMyCompany(Authentication auth) {
        String email = auth.getName();
        CompanyDto dto = companyService.getCompanyByEmail(email);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

}
