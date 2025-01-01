package com.virgo.dynamic_form.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FormResponseDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Boolean limit_one_response;
    private String creator_id;
}