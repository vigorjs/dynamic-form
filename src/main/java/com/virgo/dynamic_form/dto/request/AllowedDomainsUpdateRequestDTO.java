package com.virgo.dynamic_form.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AllowedDomainsUpdateRequestDTO {
    private List<String> allowed_domains;
}
