package com.virgo.dynamic_form.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenResponseDTO {
    private String accessToken;
    private String refreshToken;
}
