package com.b2b.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/protected")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> protectedPing() {
        return ResponseEntity.ok("pong (protected)");
    }
}
