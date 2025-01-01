package com.virgo.dynamic_form.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FormWithQuestionsAndDomainsResonseDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Boolean limit_one_response;
    private String creator_id;
    private List<String> allowed_domains;
    private List<QuestionResponseDTO> questions;
}
