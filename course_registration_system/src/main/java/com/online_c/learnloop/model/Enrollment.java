package com.online_c.learnloop.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "enrollments")
public class Enrollment {
    @Id
    private String id;
    
    private String userId;
    
    private String courseId;
    
    private String courseTitle; // Added for convenience
    
    private String status; // ACTIVE, COMPLETED, DROPPED
    
    private int progressPercentage = 0;
    
    @CreatedDate
    private LocalDateTime enrolledAt;
    
    private LocalDateTime completedAt;
} 