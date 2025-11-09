package com.b2b.controller;

import com.b2b.dto.PasswordChangeRequest;
import com.b2b.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService account;
    public AccountController(AccountService account) { this.account = account; }

    @PutMapping("/password")
    public void changeMyPassword(@Valid @RequestBody PasswordChangeRequest req,
                                 Authentication auth) {
        String email = auth.getName(); // httpBasic → email du user connecté
        account.changePassword(email, req.getCurrentPassword(), req.getNewPassword());
    }
}
