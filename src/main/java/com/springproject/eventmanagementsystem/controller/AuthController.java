package com.springproject.eventmanagementsystem.controller;

import com.springproject.eventmanagementsystem.dto.AuthLoginRequest;
import com.springproject.eventmanagementsystem.dto.AuthLoginResponse;
import com.springproject.eventmanagementsystem.dto.AuthRegistrationRequest;
import com.springproject.eventmanagementsystem.dto.AuthRegistrationResponse;
import com.springproject.eventmanagementsystem.response.SuccessResponse;
import com.springproject.eventmanagementsystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<AuthRegistrationResponse>> register(
            @Valid @RequestBody AuthRegistrationRequest request){

        AuthRegistrationResponse response = authService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                SuccessResponse.success("User registered successfully", response)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<AuthLoginResponse>> login(
            @Valid @RequestBody AuthLoginRequest request) {

        AuthLoginResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                SuccessResponse.success("Login successful",response)
        );
    }
}