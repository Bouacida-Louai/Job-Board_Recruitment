package com.hirehub.authservice.service;


import com.hirehub.authservice.dto.AuthResponse;
import com.hirehub.authservice.dto.LoginRequest;
import com.hirehub.authservice.dto.RefreshTokenRequest;
import com.hirehub.authservice.dto.RegisterRequest;
import com.hirehub.authservice.entity.User;
import com.hirehub.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);
        return generateTokens(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return generateTokens(user);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        // Look up the refresh token in Redis
        String userId = redisTemplate.opsForValue().get("refresh:" + refreshToken);
        if (userId == null) {
            throw new BadCredentialsException("Invalid or expired refresh token");
        }

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        // Delete old refresh token and issue new tokens
        redisTemplate.delete("refresh:" + refreshToken);
        return generateTokens(user);
    }

    private AuthResponse generateTokens(User user) {
        String accessToken = jwtService.generateAccessToken(
                user.getId().toString(),
                user.getRole().name());

        // Refresh token is a random UUID stored in Redis
        String refreshToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(
                "refresh:" + refreshToken,
                user.getId().toString(),
                refreshTokenExpiration,
                TimeUnit.MILLISECONDS);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration)
                .role(user.getRole().name())
                .build();
    }
}
