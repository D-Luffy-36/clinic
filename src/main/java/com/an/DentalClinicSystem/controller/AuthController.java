package com.an.DentalClinicSystem.controller;

import com.an.DentalClinicSystem.dto.request.LoginRequest;
import com.an.DentalClinicSystem.dto.request.RegisterRequest;
import com.an.DentalClinicSystem.dto.response.LoginResponse;
import com.an.DentalClinicSystem.dto.response.RegisterResponse;
import com.an.DentalClinicSystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login( @Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }

}
