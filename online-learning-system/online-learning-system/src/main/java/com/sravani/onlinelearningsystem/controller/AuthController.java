package com.sravani.onlinelearningsystem.controller;

import com.sravani.onlinelearningsystem.dto.AuthRequest;
import com.sravani.onlinelearningsystem.model.Role;
import com.sravani.onlinelearningsystem.model.User;
import com.sravani.onlinelearningsystem.repository.UserRepository;
import com.sravani.onlinelearningsystem.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Register API
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        try {
            user.setRole(Role.valueOf(request.getRole().toUpperCase())); // Accept: STUDENT, INSTRUCTOR, ADMIN
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role: must be STUDENT, INSTRUCTOR, or ADMIN");
        }

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    // ✅ Login API with role in token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByUsername(request.getUsername()).get(); // ✅ Fetch user to get role
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name()); // ✅ Generate token with role

            return ResponseEntity.ok().body("JWT Token: " + token);

        } catch (Exception e) {
            return ResponseEntity.status(403).body("Invalid username or password");
        }
    }
}
