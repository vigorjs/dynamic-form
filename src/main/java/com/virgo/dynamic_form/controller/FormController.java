package com.virgo.dynamic_form.controller;

import com.virgo.dynamic_form.dto.request.AllowedDomainsUpdateRequestDTO;
import com.virgo.dynamic_form.dto.request.FormRequestDTO;
import com.virgo.dynamic_form.dto.response.FormResponseDTO;
import com.virgo.dynamic_form.dto.response.FormWithQuestionsAndDomainsResonseDTO;
import com.virgo.dynamic_form.service.FormService;
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
@Tag(name = "Forms", description = "API for managing forms")
public class FormController {
    private final FormService formService;

    @Operation(summary = "Create new form")
    @PostMapping()
    public ResponseEntity<?> createForm(@Valid @RequestBody FormRequestDTO request) {
        Map<String, Object> response = new HashMap<>();
        FormResponseDTO form = formService.createForm(request);

        response.put("message", "Create form success");
        response.put("form", form);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all created forms")
    @GetMapping
    public ResponseEntity<?> getAllForms() {
        Map<String, Object> response = new HashMap<>();
        List<FormResponseDTO> forms = formService.getAllForms();

        response.put("message", "Get all forms success");
        response.put("forms", forms);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get form detail")
    @GetMapping("/{slug}")
    public ResponseEntity<?> getFormDetail(@PathVariable String slug) {
        Map<String, Object> response = new HashMap<>();
        FormWithQuestionsAndDomainsResonseDTO form = formService.getFormBySlug(slug);

        response.put("message", "Get form success");
        response.put("form", form);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update form allowed domains")
    @PutMapping("/{slug}/domains")
    public ResponseEntity<?> updateFormDomains(@PathVariable String slug, @RequestBody AllowedDomainsUpdateRequestDTO allowed_domains) {
        formService.updateFormDomains(slug, allowed_domains.getAllowed_domains());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Update form domains success");

        return ResponseEntity.ok(response);
    }
}