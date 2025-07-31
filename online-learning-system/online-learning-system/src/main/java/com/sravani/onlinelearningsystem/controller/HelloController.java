package com.sravani.onlinelearningsystem.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String hello(@AuthenticationPrincipal UserDetails userDetails) {
        // This will return "Hello, INSTRUCTOR!" or whatever role the user has
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .orElse("UNKNOWN");

        return "Hello, " + role + "!";
    }
}
