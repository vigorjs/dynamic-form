package com.virgo.dynamic_form.utils.constant;

public class RegexPattern {
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{5,}$";
    public static final String SLUG = "^[a-z0-9-\\.]+$";
}
