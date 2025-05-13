package com.online_c.learnloop.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    
    private String fullName;
    
    @Indexed(unique = true)
    private String email;
    
    private String password;
    
    private String phone;
    
    private LocalDate dateOfBirth;
    
    private String education;
    
    private String profession;
    
    private Set<String> interests = new HashSet<>();
    
    private String bio;
    
    private String linkedinProfile;
    
    private String githubProfile;
    
    private String personalWebsite;
    
    private String location;
    
    private String profilePhotoUrl;
    
    private String userType = "STUDENT"; // STUDENT, INSTRUCTOR, ADMIN
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private boolean profileCompleted = false;
    
    private int profileCompletionPercentage = 0;
    
    // Calculate profile completion percentage
    public void calculateProfileCompletionPercentage() {
        int totalFields = 10; // Number of important profile fields
        int completedFields = 0;
        
        if (fullName != null && !fullName.isEmpty()) completedFields++;
        if (email != null && !email.isEmpty()) completedFields++;
        if (phone != null && !phone.isEmpty()) completedFields++;
        if (dateOfBirth != null) completedFields++;
        if (education != null && !education.isEmpty()) completedFields++;
        if (profession != null && !profession.isEmpty()) completedFields++;
        if (interests != null && !interests.isEmpty()) completedFields++;
        if (bio != null && !bio.isEmpty()) completedFields++;
        if (location != null && !location.isEmpty()) completedFields++;
        if (profilePhotoUrl != null && !profilePhotoUrl.isEmpty()) completedFields++;
        
        this.profileCompletionPercentage = (completedFields * 100) / totalFields;
        this.profileCompleted = this.profileCompletionPercentage == 100;
    }
}
