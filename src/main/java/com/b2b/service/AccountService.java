package com.b2b.service;

public interface AccountService {
    void changePassword(String email, String currentRaw, String newRaw);
}
