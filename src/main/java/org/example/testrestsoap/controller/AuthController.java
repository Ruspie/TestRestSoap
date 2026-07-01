package org.example.testrestsoap.controller;

import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.dto.AuthRequestDto;
import org.example.testrestsoap.dto.JwtResponseDto;
import org.example.testrestsoap.dto.RefreshRequestDto;
import org.example.testrestsoap.entity.jpa.RefreshTokenEntity;
import org.example.testrestsoap.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody AuthRequestDto request) {
        // Проверяем логин и пароль через встроенный AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponseDto(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refresh(@RequestBody RefreshRequestDto request) {
        RefreshTokenEntity verifiedEntity = jwtService.verifyRefreshToken(request.getRefreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(verifiedEntity.getUsername());

        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponseDto(newAccessToken, newRefreshToken));
    }
}
