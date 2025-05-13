package com.online_c.learnloop.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.online_c.learnloop.model.Course;
import com.online_c.learnloop.repository.CourseRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final CourseRepository courseRepository;
    private final MongoTemplate mongoTemplate;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Check if courses collection exists and has data
            long count = courseRepository.count();
            
            if (count == 0) {
                System.out.println("No courses found. Creating sample courses...");
                
                // Create sample courses
                List<Course> courses = createSampleCourses();
                
                // Save courses to the database
                courseRepository.saveAll(courses);
                
                System.out.println("Sample courses created successfully!");
            } else {
                System.out.println("Database already contains " + count + " courses. Skipping data initialization.");
            }
        };
    }
    
    private List<Course> createSampleCourses() {
        List<Course> courses = new ArrayList<>();
        
        // Java Programming Course
        Course javaCourse = new Course();
        javaCourse.setTitle("Java Programming Masterclass");
        javaCourse.setDescription("Learn Java from scratch to advanced. This comprehensive course covers core Java, OOP concepts, data structures, and practical projects to help you become a proficient Java developer.");
        javaCourse.setCategory("Programming");
        javaCourse.setImageUrl("https://placehold.co/600x400?text=Java");
        javaCourse.setDurationWeeks(12);
        javaCourse.setLevel("Beginner to Advanced");
        javaCourse.setOriginalPrice(new BigDecimal("99.99"));
        javaCourse.setDiscountedPrice(new BigDecimal("79.99"));
        javaCourse.setTags(Arrays.asList("Java", "Programming", "OOP", "Backend"));
        javaCourse.setInstructorId("instructor1");
        javaCourse.setAverageRating(4.7f);
        javaCourse.setReviewsCount(425);
        javaCourse.setBestseller(true);
        courses.add(javaCourse);
        
        // Web Development Course
        Course webDevCourse = new Course();
        webDevCourse.setTitle("Full Stack Web Development");
        webDevCourse.setDescription("Master both frontend and backend web development with this all-in-one course. Learn HTML, CSS, JavaScript, React, Node.js, Express, and MongoDB to build complete web applications.");
        webDevCourse.setCategory("Web Development");
        webDevCourse.setImageUrl("https://placehold.co/600x400?text=WebDev");
        webDevCourse.setDurationWeeks(16);
        webDevCourse.setLevel("Intermediate");
        webDevCourse.setOriginalPrice(new BigDecimal("129.99"));
        webDevCourse.setDiscountedPrice(new BigDecimal("99.99"));
        webDevCourse.setTags(Arrays.asList("HTML", "CSS", "JavaScript", "React", "Node.js", "MongoDB"));
        webDevCourse.setInstructorId("instructor2");
        webDevCourse.setAverageRating(4.8f);
        webDevCourse.setReviewsCount(738);
        webDevCourse.setBestseller(true);
        courses.add(webDevCourse);
        
        // Python Course
        Course pythonCourse = new Course();
        pythonCourse.setTitle("Python for Data Science and Machine Learning");
        pythonCourse.setDescription("Dive into the world of data science and machine learning using Python. Learn data analysis, visualization, and implement machine learning algorithms for real-world problems.");
        pythonCourse.setCategory("Data Science");
        pythonCourse.setImageUrl("https://placehold.co/600x400?text=Python");
        pythonCourse.setDurationWeeks(14);
        pythonCourse.setLevel("Intermediate to Advanced");
        pythonCourse.setOriginalPrice(new BigDecimal("149.99"));
        pythonCourse.setDiscountedPrice(new BigDecimal("119.99"));
        pythonCourse.setTags(Arrays.asList("Python", "Data Science", "Machine Learning", "AI"));
        pythonCourse.setInstructorId("instructor3");
        pythonCourse.setAverageRating(4.9f);
        pythonCourse.setReviewsCount(892);
        pythonCourse.setBestseller(true);
        courses.add(pythonCourse);
        
        // Mobile Development Course
        Course mobileCourse = new Course();
        mobileCourse.setTitle("Android App Development with Kotlin");
        mobileCourse.setDescription("Learn to build modern Android applications using Kotlin. This course covers Android Studio, UI design, data storage, APIs, and publishing your app to the Google Play Store.");
        mobileCourse.setCategory("Mobile Development");
        mobileCourse.setImageUrl("https://placehold.co/600x400?text=Android");
        mobileCourse.setDurationWeeks(10);
        mobileCourse.setLevel("Intermediate");
        mobileCourse.setOriginalPrice(new BigDecimal("89.99"));
        mobileCourse.setDiscountedPrice(new BigDecimal("69.99"));
        mobileCourse.setTags(Arrays.asList("Android", "Kotlin", "Mobile", "App Development"));
        mobileCourse.setInstructorId("instructor4");
        mobileCourse.setAverageRating(4.6f);
        mobileCourse.setReviewsCount(315);
        mobileCourse.setNew(true);
        courses.add(mobileCourse);
        
        // Cybersecurity Course
        Course securityCourse = new Course();
        securityCourse.setTitle("Ethical Hacking and Cybersecurity");
        securityCourse.setDescription("Learn ethical hacking and cybersecurity from basics to advanced. This course covers network security, vulnerability assessment, penetration testing, and security best practices.");
        securityCourse.setCategory("Cybersecurity");
        securityCourse.setImageUrl("https://placehold.co/600x400?text=Security");
        securityCourse.setDurationWeeks(12);
        securityCourse.setLevel("Beginner to Intermediate");
        securityCourse.setOriginalPrice(new BigDecimal("119.99"));
        securityCourse.setDiscountedPrice(new BigDecimal("89.99"));
        securityCourse.setTags(Arrays.asList("Security", "Ethical Hacking", "Network", "Pentesting"));
        securityCourse.setInstructorId("instructor5");
        securityCourse.setAverageRating(4.7f);
        securityCourse.setReviewsCount(512);
        securityCourse.setNew(true);
        courses.add(securityCourse);
        
        // UI/UX Design Course
        Course designCourse = new Course();
        designCourse.setTitle("UI/UX Design Fundamentals");
        designCourse.setDescription("Master the principles of user interface and user experience design. Learn to create visually appealing, user-friendly designs using industry-standard tools like Figma and Adobe XD.");
        designCourse.setCategory("Design");
        designCourse.setImageUrl("https://placehold.co/600x400?text=UI/UX");
        designCourse.setDurationWeeks(8);
        designCourse.setLevel("Beginner");
        designCourse.setOriginalPrice(new BigDecimal("79.99"));
        designCourse.setDiscountedPrice(new BigDecimal("59.99"));
        designCourse.setTags(Arrays.asList("UI", "UX", "Design", "Figma", "Adobe XD"));
        designCourse.setInstructorId("instructor6");
        designCourse.setAverageRating(4.5f);
        designCourse.setReviewsCount(287);
        courses.add(designCourse);
        
        return courses;
    }
} 