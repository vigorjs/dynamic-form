package com.virgo.dynamic_form.utils.mapper;

import com.virgo.dynamic_form.dto.request.ResponseRequestDTO;
import com.virgo.dynamic_form.model.meta.Answer;
import com.virgo.dynamic_form.model.meta.Question;
import com.virgo.dynamic_form.model.meta.Response;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {

    public Answer toEntity(ResponseRequestDTO.AnswerRequestDTO dto, Response response, Question question) {
        return Answer.builder()
                .value(dto.getValue())
                .response(response)
                .question(question)
                .build();
    }

    public ResponseRequestDTO.AnswerRequestDTO toDTO(Answer answer) {
        ResponseRequestDTO.AnswerRequestDTO dto = new ResponseRequestDTO.AnswerRequestDTO();
        dto.setQuestion_id(answer.getQuestion().getId());
        dto.setValue(answer.getValue());
        return dto;
    }
}