package com.virgo.dynamic_form.controller;

import com.virgo.dynamic_form.dto.request.UserRequestDTO;
import com.virgo.dynamic_form.dto.request.UserUpdateRequestDTO;
import com.virgo.dynamic_form.model.meta.global.UserEntity;
import com.virgo.dynamic_form.service.UserService;
import com.virgo.dynamic_form.utils.constant.EndpointConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(EndpointConstant.USER_API)
@RequiredArgsConstructor
@Tag(name = "Users", description = "API for managing users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserEntity> users = userService.getAll();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Get all users success");
        response.put("users", users);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        UserEntity user = userService.getById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Get user success");
        response.put("user", user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create new user")
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO request) {
        UserEntity createdUser = userService.create(request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Create user success");
        response.put("user", createdUser);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user")
    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateRequestDTO request) {
        UserEntity updatedUser = userService.update(request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Update user success");
        response.put("user", updatedUser);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user profile")
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        UserEntity user = userService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Get profile success");
        response.put("user", user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user by email")
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        UserEntity user = userService.getByEmail(email);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Get user success");
        response.put("user", user);
        return ResponseEntity.ok(response);
    }
}