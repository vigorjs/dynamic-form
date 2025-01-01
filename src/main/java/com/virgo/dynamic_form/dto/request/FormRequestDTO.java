package com.virgo.dynamic_form.dto.request;

import com.virgo.dynamic_form.utils.constant.RegexPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class FormRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Slug is required")
    @Pattern(regexp = RegexPattern.SLUG, message = "Slug must be alphanumeric with only dash and dot")
    private String slug;

    private String description;
    private Boolean limit_one_response;
    private List<String> allowed_domains;
}