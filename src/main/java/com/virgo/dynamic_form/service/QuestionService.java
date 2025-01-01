package com.virgo.dynamic_form.service;

import com.virgo.dynamic_form.dto.request.QuestionRequestDTO;
import com.virgo.dynamic_form.dto.response.QuestionResponseDTO;

public interface QuestionService {
    QuestionResponseDTO addQuestion(String formSlug, QuestionRequestDTO request);
    void removeQuestion(String formSlug, Long questionId);
    void validateQuestionAccess(String formSlug, Long questionId);
}
