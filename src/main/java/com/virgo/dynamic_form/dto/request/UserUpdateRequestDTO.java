package com.virgo.dynamic_form.dto.request;

import com.virgo.dynamic_form.utils.constant.RegexPattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
public class UserUpdateRequestDTO {
    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Pattern(
            regexp = RegexPattern.PASSWORD,
            message = "Must be at least 5 characters long containing uppercase, lowercase, number, and special character"
    )
    private String password;
}
