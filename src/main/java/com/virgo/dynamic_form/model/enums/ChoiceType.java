package com.virgo.dynamic_form.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ChoiceType {
    @JsonProperty("multiple choice")
    MULTIPLE_CHOICE,
    @JsonProperty("short answer")
    SHORT_ANSWER,
    @JsonProperty("time")
    TIME,
    @JsonProperty("date")
    DATE,
    @JsonProperty("dropdown")
    DROPDOWN,
    @JsonProperty("paragraph")
    PARAGRAPH,
    @JsonProperty("checkboxes")
    CHECKBOXES;
}
