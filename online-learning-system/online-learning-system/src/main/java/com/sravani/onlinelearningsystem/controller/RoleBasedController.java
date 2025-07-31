package com.sravani.onlinelearningsystem.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RoleBasedController {

    @GetMapping("/student/dashboard")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentDashboard() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "Welcome Student: " + userDetails.getUsername();
    }

    @GetMapping("/instructor/dashboard")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public String instructorDashboard() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "Welcome Instructor: " + userDetails.getUsername();
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "Welcome Admin: " + userDetails.getUsername();
    }
}
