package com.virgo.dynamic_form.dto.request;

import com.virgo.dynamic_form.model.enums.ChoiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class QuestionRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Choice type is required")
    private ChoiceType choice_type;

    private List<String> choices;

    private Boolean is_required;
}