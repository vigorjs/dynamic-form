package com.virgo.dynamic_form.utils.mapper;

import com.virgo.dynamic_form.dto.request.QuestionRequestDTO;
import com.virgo.dynamic_form.dto.response.QuestionResponseDTO;
import com.virgo.dynamic_form.model.meta.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {
    public QuestionResponseDTO toDTO(Question question) {
        return QuestionResponseDTO.builder()
                .id(question.getId())
                .name(question.getName())
                .choice_type(question.getChoiceType())
                .choices(question.getChoices() != null && !question.getChoices().isEmpty()
                        ? question.getChoices() : null)
                .is_required(question.getIsRequired())
                .form_id(question.getForm().getId())
                .build();
    }

    public Question toEntity(QuestionRequestDTO dto) {
        return Question.builder()
                .name(dto.getName())
                .choiceType(dto.getChoice_type())
                .choices(dto.getChoices() != null ? String.join(",", dto.getChoices()) : "")
                .isRequired(dto.getIs_required())
                .build();
    }

}