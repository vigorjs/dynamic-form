package com.virgo.dynamic_form.controller;

import com.virgo.dynamic_form.dto.request.QuestionRequestDTO;
import com.virgo.dynamic_form.dto.response.QuestionResponseDTO;
import com.virgo.dynamic_form.service.QuestionService;
import com.virgo.dynamic_form.utils.constant.EndpointConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(EndpointConstant.FORM_API)
@RequiredArgsConstructor
@Tag(name = "Questions", description = "API for managing form questions")
public class QuestionController {
    private final QuestionService questionService;

    @Operation(summary = "Add question to form")
    @PostMapping("/{form_slug}/questions")
    public ResponseEntity<?> addQuestion(@PathVariable String form_slug, @Valid @RequestBody QuestionRequestDTO request) {
        Map<String, Object> response = new HashMap<>();
        QuestionResponseDTO question = questionService.addQuestion(form_slug, request);

        response.put("question", question);
        response.put("message", "Add question success");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove question from form")
    @DeleteMapping("/{form_slug}/questions/{question_id}")
    public ResponseEntity<?> removeQuestion(@PathVariable String form_slug, @PathVariable Long question_id) {
        questionService.removeQuestion(form_slug, question_id);

        return ResponseEntity.ok(Map.of("message", "Remove question success"));
    }
}