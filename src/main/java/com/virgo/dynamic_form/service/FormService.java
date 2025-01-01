package com.virgo.dynamic_form.service;

import com.virgo.dynamic_form.dto.request.FormRequestDTO;
import com.virgo.dynamic_form.dto.response.FormResponseDTO;
import com.virgo.dynamic_form.dto.response.FormWithQuestionsAndDomainsResonseDTO;

import java.util.List;

public interface FormService {
    FormResponseDTO createForm(FormRequestDTO request);
    List<FormResponseDTO> getAllForms();
    FormWithQuestionsAndDomainsResonseDTO getFormBySlug(String slug);
    boolean isUserAllowedToSubmit(String formSlug, String userEmail);
    void updateFormDomains(String formSlug, List<String> domains);
    void validateFormAccess(String formSlug);
}
