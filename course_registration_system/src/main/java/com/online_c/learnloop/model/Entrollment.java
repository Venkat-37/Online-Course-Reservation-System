package com.online_c.learnloop.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "enrollments")
public class Entrollment {
    @Id
    private String id;
    
    private String userId;
    
    private String courseId;
    
    // This field won't be saved to the database, just used for UI display
    private transient String courseTitle;
    
    private String status; // ACTIVE, COMPLETED, DROPPED
    
    private int progressPercentage = 0;
    
    @CreatedDate
    private LocalDateTime enrolledAt;
    
    private LocalDateTime completedAt;
}

