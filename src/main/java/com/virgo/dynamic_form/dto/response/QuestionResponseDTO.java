package com.virgo.dynamic_form.dto.response;

import com.virgo.dynamic_form.model.enums.ChoiceType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionResponseDTO {
    private Long id;
    private String name;
    private ChoiceType choice_type;
    private String choices;
    private Boolean is_required;
    private Long form_id;
}