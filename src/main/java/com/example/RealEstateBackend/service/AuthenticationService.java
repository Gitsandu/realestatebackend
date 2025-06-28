package com.example.RealEstateBackend.service;

import com.example.RealEstateBackend.dto.auth.AuthenticationRequest;
import com.example.RealEstateBackend.dto.auth.AuthenticationResponse;
import com.example.RealEstateBackend.dto.auth.RegisterRequest;
import com.example.RealEstateBackend.exception.TokenRefreshException;
import com.example.RealEstateBackend.model.RefreshToken;
import com.example.RealEstateBackend.model.User;
import com.example.RealEstateBackend.model.User.UserRole;
import com.example.RealEstateBackend.repository.UserRepository;
import com.example.RealEstateBackend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already in use");
        }

        // Create new user
        var user = User.builder()
                .name(request.getFirstName() + " " + request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : UserRole.USER)
                .enabled(true) // Set to false if email verification is required
                .build();

        // Save user to database
        var savedUser = userRepository.save(user);
        
        // Generate JWT token
        var jwtToken = jwtService.generateToken(user);
        
        // Create refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        
        // Return authentication response
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .fullName(savedUser.getName())
                .expiresIn(jwtService.getTokenExpirationTime() / 1000) // Convert to seconds
                .refreshExpiresIn(refreshToken.getExpiryDate().toEpochMilli() - System.currentTimeMillis())
                .build();
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        // Get user details
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));
        
        if (!user.isEnabled()) {
            throw new IllegalStateException("User account is disabled");
        }
        
        // Generate JWT token
        var jwtToken = jwtService.generateToken(user);
        
        // Create refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        
        // Return authentication response
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .email(user.getEmail())
                .role(user.getRole().name())
                .fullName(user.getName())
                .expiresIn(jwtService.getTokenExpirationTime() / 1000) // Convert to seconds
                .build();
    }
    
    @Transactional
    public AuthenticationResponse refreshToken(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(user -> {
                String token = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                        .accessToken(token)
                        .refreshToken(refreshToken)
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .fullName(user.getName())
                        .expiresIn(jwtService.getTokenExpirationTime() / 1000)
                        .build();
            })
            .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database!"));
    }
}
