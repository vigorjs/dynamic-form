package com.virgo.dynamic_form.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponseDTO {

    private String name;
    private String email;
    private String accessToken;
    private String refreshToken;

}
