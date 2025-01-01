package com.virgo.dynamic_form.controller;

import com.virgo.dynamic_form.dto.request.ResponseRequestDTO;
import com.virgo.dynamic_form.dto.response.ResponseResponseDTO;
import com.virgo.dynamic_form.service.ResponseService;
import com.virgo.dynamic_form.utils.constant.EndpointConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(EndpointConstant.FORM_API)
@RequiredArgsConstructor
@Tag(name = "Responses", description = "API for managing form responses")
public class ResponseController {
    private final ResponseService responseService;

    @Operation(summary = "Submit form response")
    @PostMapping("/{formSlug}/responses")
    public ResponseEntity<?> submitResponse(@PathVariable String formSlug, @Valid @RequestBody ResponseRequestDTO request) {
        responseService.submitResponse(formSlug, request);
        return ResponseEntity.ok(Map.of("message", "Submit response success"));
    }

    @Operation(summary = "Get all form responses")
    @GetMapping("/{formSlug}/responses")
    public ResponseEntity<?> getAllResponses(@PathVariable String formSlug) {
        Map<String, Object> response = new HashMap<>();
        List<ResponseResponseDTO> responses = responseService.getAllResponses(formSlug);

        response.put("responses", responses);
        response.put("message", "Get responses success");

        return ResponseEntity.ok(response);
    }
}