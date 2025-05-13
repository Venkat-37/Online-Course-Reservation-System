package com.online_c.learnloop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "courses")
public class Course {
    @Id
    private String id;
    
    private String title;
    
    private String description;
    
    private String category;
    
    private String imageUrl;
    
    private int durationWeeks;
    
    private String level; // Beginner, Intermediate, Advanced, All Levels
    
    private BigDecimal originalPrice;
    
    private BigDecimal discountedPrice;
    
    private List<String> tags = new ArrayList<>();
    
    private String instructorId;
    
    private float averageRating = 0.0f;
    
    private int reviewsCount = 0;
    
    private boolean isBestseller = false;
    
    private boolean isNew = false;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
