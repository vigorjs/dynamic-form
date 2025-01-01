package com.virgo.dynamic_form.controller;

import com.virgo.dynamic_form.dto.request.AuthenticationRequestDTO;
import com.virgo.dynamic_form.dto.request.RegisterRequestDTO;
import com.virgo.dynamic_form.dto.response.AuthenticationResponseDTO;
import com.virgo.dynamic_form.dto.response.RefreshTokenResponseDTO;
import com.virgo.dynamic_form.service.AuthService;
import com.virgo.dynamic_form.utils.constant.EndpointConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(EndpointConstant.AUTH_API)
@RequiredArgsConstructor
@Tag(name = "Auth", description = "API for authentication")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequestDTO request) {
        Map<String, Object> response = new HashMap<>();
        AuthenticationResponseDTO authResponse = authService.login(request);

        response.put("message", "Login success");
        response.put("user", authResponse);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
        Map<String, Object> response = new HashMap<>();
        AuthenticationResponseDTO authResponse = authService.register(request);

        response.put("message", "Register success");
        response.put("user", authResponse);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Authorization");

        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        RefreshTokenResponseDTO response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Logout user")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok(Map.of("message", "Logout success"));
    }
}
