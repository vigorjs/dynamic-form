package com.virgo.dynamic_form.service;

import com.virgo.dynamic_form.dto.request.AuthenticationRequestDTO;
import com.virgo.dynamic_form.dto.request.RegisterRequestDTO;
import com.virgo.dynamic_form.dto.response.AuthenticationResponseDTO;
import com.virgo.dynamic_form.dto.response.RefreshTokenResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    AuthenticationResponseDTO register(RegisterRequestDTO request);

    AuthenticationResponseDTO login(AuthenticationRequestDTO loginRequestDTO);

    void logout(HttpServletRequest request);

    RefreshTokenResponseDTO refreshToken(String refreshToken);
}
