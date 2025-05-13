package com.online_c.learnloop.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.online_c.learnloop.model.Course;
import com.online_c.learnloop.model.Entrollment;
import com.online_c.learnloop.model.User;
import com.online_c.learnloop.repository.Entrollmentrepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    
    private final Entrollmentrepository enrollmentRepository;
    private final UserService userService;
    private final CourseService courseService;

    public Object enrollUserInCourse(String userId, String courseId) {
        // Check if user is already enrolled
        Optional<Entrollment> existingEnrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
        if (existingEnrollment.isPresent()) {
            return existingEnrollment.get();
        }
        
        // Get user and course
        User user = (User) userService.getUserById(userId);
        Course course = courseService.getCourseById(courseId);
        
        // Create new enrollment
        Entrollment enrollment = new Entrollment();
        enrollment.setUserId(userId);
        enrollment.setCourseId(courseId);
        enrollment.setStatus("ACTIVE");
        enrollment.setProgressPercentage(0);
        
        return enrollmentRepository.save(enrollment);
    }
    
    public List<Entrollment> getUserEnrollments(String userId) {
        List<Entrollment> enrollments = enrollmentRepository.findByUserId(userId);
        
        // Enrich enrollments with course details
        for (Entrollment enrollment : enrollments) {
            try {
                Course course = courseService.getCourseById(enrollment.getCourseId());
                // Since Entrollment doesn't have a courseTitle field, we can add it as a transient property
                enrollment.setCourseTitle(course.getTitle());
            } catch (Exception e) {
                // Handle case where course might have been deleted
                enrollment.setCourseTitle("Unknown Course");
            }
        }
        
        return enrollments;
    }
    
    public Object updateEnrollmentProgress(String enrollmentId, String userId, int progressPercentage) {
        Entrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
        
        // Verify the enrollment belongs to the user
        if (!enrollment.getUserId().equals(userId)) {
            throw new SecurityException("User does not have permission to update this enrollment");
        }
        
        enrollment.setProgressPercentage(progressPercentage);
        
        // If progress is 100%, mark as completed
        if (progressPercentage == 100) {
            enrollment.setStatus("COMPLETED");
            enrollment.setCompletedAt(LocalDateTime.now());
        }
        
        return enrollmentRepository.save(enrollment);
    }
    
    public boolean isUserEnrolled(String userId, String courseId) {
        return enrollmentRepository.findByUserIdAndCourseId(userId, courseId).isPresent();
    }
}
