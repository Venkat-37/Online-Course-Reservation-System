package com.online_c.learnloop.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String token;
    private String id;
    private String email;
    private String fullName;
    private String userType;
    private int profileCompletionPercentage;
}