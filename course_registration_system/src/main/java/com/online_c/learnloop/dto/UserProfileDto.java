package com.online_c.learnloop.dto;

import java.time.LocalDate;
import java.util.Set;

import lombok.Data;

@Data
public class UserProfileDto {
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String education;
    private String profession;
    private Set<String> interests;
    private String bio;
    private String linkedinProfile;
    private String githubProfile;
    private String personalWebsite;
    private String location;
    private int profileCompletionPercentage;
}