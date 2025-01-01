package com.virgo.dynamic_form.service.serviceImpl;

import com.virgo.dynamic_form.config.security.JWTUtils;
import com.virgo.dynamic_form.dto.request.AuthenticationRequestDTO;
import com.virgo.dynamic_form.dto.request.RegisterRequestDTO;
import com.virgo.dynamic_form.dto.response.AuthenticationResponseDTO;
import com.virgo.dynamic_form.dto.response.RefreshTokenResponseDTO;
import com.virgo.dynamic_form.model.enums.TokenType;
import com.virgo.dynamic_form.model.meta.global.Token;
import com.virgo.dynamic_form.model.meta.global.UserEntity;
import com.virgo.dynamic_form.repository.TokenRepository;
import com.virgo.dynamic_form.repository.UserRepository;
import com.virgo.dynamic_form.service.AuthService;
import com.virgo.dynamic_form.utils.advisers.exception.DuplicateEntryException;
import com.virgo.dynamic_form.utils.advisers.exception.InvalidTokenException;
import com.virgo.dynamic_form.utils.advisers.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEntryException("Email already registered");
        }
        if (userRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateEntryException("Name already taken");
        }

        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtUtils.generateToken(savedUser.getName(), TokenType.ACCESS);
        var refreshToken = jwtUtils.generateToken(savedUser.getName(), TokenType.REFRESH);
        saveUserToken(user, jwtToken);
        saveUserToken(user, refreshToken);
        return AuthenticationResponseDTO.builder()
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getEmail(), authenticationRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserEntity user = (UserEntity) authentication.getPrincipal();
        var accessToken = jwtUtils.generateToken(user.getEmail(), TokenType.ACCESS);
        var refreshToken = jwtUtils.generateToken(user.getEmail(), TokenType.REFRESH);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        saveUserToken(user, refreshToken);

        return AuthenticationResponseDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public RefreshTokenResponseDTO refreshToken(String refreshToken) {
        if (jwtUtils.validateToken(refreshToken)) {
            String name = jwtUtils.getEmailFromToken(refreshToken);

            Optional<UserEntity> userOptional = userRepository.findByName(name);
            UserDetails userDetails = userOptional
                    .map(user -> User.withUsername(user.getName()).password(user.getPassword()).build())
                    .orElseThrow(() -> new NotFoundException("User not found"));

            String accessToken = jwtUtils.generateToken(userDetails.getUsername(), TokenType.ACCESS);
            String newRefreshToken = jwtUtils.generateToken(userDetails.getUsername(), TokenType.REFRESH);
            UserEntity user = userOptional.orElseThrow(() -> new NotFoundException("User Not Found"));
            revokeAllUserTokens(user);
            saveUserToken(user, accessToken);
            saveUserToken(user, newRefreshToken);

            return RefreshTokenResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new InvalidTokenException("Invalid refresh token");
        }
    }

    @Override
    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);

        var storedToken = tokenRepository.findByToken(jwt).orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }

    private void saveUserToken(UserEntity user, String jwtToken) {
        TokenType tokenType = jwtUtils.getTokenType(jwtToken);
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(tokenType)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
