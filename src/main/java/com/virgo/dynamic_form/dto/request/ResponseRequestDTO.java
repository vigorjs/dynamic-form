package com.virgo.dynamic_form.dto.request;

import com.virgo.dynamic_form.model.meta.Answer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResponseRequestDTO {

    @NotNull(message = "Answer field is required")
    private List<AnswerRequestDTO> answers;

    @Data
    public static class AnswerRequestDTO{
        private Long question_id;
        private String value;
    }

}