package com.example.RealEstateBackend.controller;

import com.example.RealEstateBackend.dto.auth.AuthenticationRequest;
import com.example.RealEstateBackend.dto.auth.AuthenticationResponse;
import com.example.RealEstateBackend.dto.auth.RegisterRequest;
import com.example.RealEstateBackend.dto.auth.TokenRefreshRequest;
import com.example.RealEstateBackend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling authentication-related endpoints.
 * User management endpoints are available in UserController.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Register a new user
     * @param request The registration request containing user details
     * @return Authentication response with JWT tokens
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    /**
     * Authenticate a user and return JWT tokens
     * @param request The authentication request containing credentials
     * @return Authentication response with JWT tokens
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    /**
     * Refresh an access token using a refresh token
     * @param request The refresh token request
     * @return New authentication response with fresh tokens
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        return ResponseEntity.ok(authenticationService.refreshToken(request.getRefreshToken()));
    }
}
