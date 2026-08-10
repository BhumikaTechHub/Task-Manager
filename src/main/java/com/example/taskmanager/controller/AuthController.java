package com.example.taskmanager.controller;

import com.example.taskmanager.dto.AuthResponse;
import com.example.taskmanager.dto.LoginRequest;
import com.example.taskmanager.dto.RegisterRequest;
import com.example.taskmanager.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication",
        description = "User Registration and Login"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {

        this.authService = authService;

    }
    @Operation(summary = "Register User")
    @PostMapping("/register")
    public ResponseEntity<String> register(

            @Valid @RequestBody RegisterRequest request

    ) {

        return ResponseEntity.ok(

                authService.register(request)

        );

    }

    @Operation(summary = "Login User")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(

            @Valid @RequestBody LoginRequest request

    ) {

        return ResponseEntity.ok(

                authService.login(request)

        );

    }

}