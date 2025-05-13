package com.online_c.learnloop.repository;

import com.online_c.learnloop.model.Entrollment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface Entrollmentrepository extends MongoRepository<Entrollment, String> {
    List<Entrollment> findByUserId(String userId);
    Optional<Entrollment> findByUserIdAndCourseId(String userId, String courseId);
}