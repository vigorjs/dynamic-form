package com.virgo.dynamic_form.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ResponseResponseDTO {
    private Map<String, String> answers;
    private String date;
    private UserResponseDTO user;

    @Data
    @Builder
    public static class UserResponseDTO {
        private String id;
        private String name;
        private String email;
        private String email_verified_at;
    }
}