package com.online_c.learnloop.repository;

import com.online_c.learnloop.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String> {
    Page<Course> findAllByCategory(String category, Pageable pageable);
    List<Course> findTop4ByOrderByAverageRatingDesc();
}
