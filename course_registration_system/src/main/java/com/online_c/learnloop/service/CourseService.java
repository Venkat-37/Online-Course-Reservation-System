package com.online_c.learnloop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.online_c.learnloop.model.Course;
import com.online_c.learnloop.repository.CourseRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {
    
    private final CourseRepository courseRepository;

    public List<Course> getFeaturedCourses() {
        return courseRepository.findTop4ByOrderByAverageRatingDesc();
    }
    
    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }
    
    public Course getCourseById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }
    
    public Page<Course> getCoursesByCategory(String category, Pageable pageable) {
        return courseRepository.findAllByCategory(category, pageable);
    }
}
