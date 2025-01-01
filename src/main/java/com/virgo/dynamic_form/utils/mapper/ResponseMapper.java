package com.virgo.dynamic_form.utils.mapper;

import com.virgo.dynamic_form.dto.response.ResponseResponseDTO;
import com.virgo.dynamic_form.model.meta.Response;
import com.virgo.dynamic_form.model.meta.UserEntity;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseMapper {

    public ResponseResponseDTO toDTO(Response response) {

        Map<String, String> answersMap = new HashMap<>();

        if (response.getAnswers() != null) {
            response.getAnswers().forEach(answer -> {
                if (answer.getQuestion() != null) {
                    answersMap.put(answer.getQuestion().getName(), answer.getValue());
                }
            });
        }

        UserEntity user = response.getUser();
        ResponseResponseDTO.UserResponseDTO userDTO = null;
        if (user != null) {
            userDTO = ResponseResponseDTO.UserResponseDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .email_verified_at(user.getEmailVerifiedAt() != null ? user.getEmailVerifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
                    .build();
        }

        return ResponseResponseDTO.builder()
                .answers(answersMap)
                .date(response.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .user(userDTO)
                .build();
    }

}