package com.virgo.dynamic_form.service;

import com.virgo.dynamic_form.dto.request.ResponseRequestDTO;
import com.virgo.dynamic_form.dto.response.ResponseResponseDTO;

import java.util.List;

public interface ResponseService {
    void submitResponse(String formSlug, ResponseRequestDTO request);
    List<ResponseResponseDTO> getAllResponses(String formSlug);
    boolean hasUserSubmitted(String formSlug, String userId);
}
