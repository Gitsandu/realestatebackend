package com.example.RealEstateBackend.service;

import com.example.RealEstateBackend.dto.auth.AuthenticationRequest;
import com.example.RealEstateBackend.dto.auth.AuthenticationResponse;
import com.example.RealEstateBackend.dto.auth.RegisterRequest;
import com.example.RealEstateBackend.model.User;
import com.example.RealEstateBackend.model.User.UserRole;
import com.example.RealEstateBackend.repository.UserRepository;
import com.example.RealEstateBackend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // Create new user
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : UserRole.USER)
                .enabled(true) // Set to false if email verification is required
                .build();

        // Save user to database
        var savedUser = userRepository.save(user);
        
        // Generate JWT token
        var jwtToken = jwtService.generateToken(user);
        
        // Return authentication response
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .expiresIn(jwtService.getTokenExpirationTime() / 1000) // Convert to seconds
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .fullName(savedUser.getFirstName() + " " + savedUser.getLastName())
                .build();
    }

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
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Generate JWT token
        var jwtToken = jwtService.generateToken(user);
        
        // Return authentication response
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .expiresIn(jwtService.getTokenExpirationTime() / 1000) // Convert to seconds
                .email(user.getEmail())
                .role(user.getRole().name())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .build();
    }
}
