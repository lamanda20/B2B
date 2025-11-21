package com.b2b.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashTool {
    public static void main(String[] args) {
        var encoder = new BCryptPasswordEncoder();
        String raw = "Admin@2025";
        String hash = encoder.encode(raw);
        System.out.println("Hash pour " + raw + " = " + hash);
    }
}
