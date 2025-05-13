package com.online_c.learnloop.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.online_c.learnloop.model.Course;
import com.online_c.learnloop.security.UserDetailsImpl;
import com.online_c.learnloop.service.CourseService;
import com.online_c.learnloop.service.EnrollmentService;
import com.online_c.learnloop.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final EnrollmentService enrollmentService;

    @GetMapping
    public String listCourses(@PageableDefault(size = 9) Pageable pageable, Model model) {
        Page<Course> courses = courseService.getAllCourses(pageable);
        model.addAttribute("courses", courses);
        return "courses/list";
    }

    @GetMapping("/{id}")
    public String courseDetails(@PathVariable String id, 
                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                              Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        
        // Check if user is enrolled
        boolean isEnrolled = false;
        if (userDetails != null) {
            isEnrolled = enrollmentService.isUserEnrolled(userDetails.getId(), id);
        }
        model.addAttribute("isEnrolled", isEnrolled);
        
        return "courses/details";
    }

    @GetMapping("/category/{category}")
    public String coursesByCategory(@PathVariable String category,
                                  @PageableDefault(size = 9) Pageable pageable,
                                  Model model) {
        Page<Course> courses = courseService.getCoursesByCategory(category, pageable);
        model.addAttribute("courses", courses);
        model.addAttribute("category", category);
        return "courses/by-category";
    }

    @PostMapping("/{id}/enroll")
    public String enrollCourse(@PathVariable String id,
                             @AuthenticationPrincipal UserDetailsImpl userDetails,
                             RedirectAttributes redirectAttributes) {
        enrollmentService.enrollUserInCourse(userDetails.getId(), id);
        redirectAttributes.addAttribute("enrolled", true);
        return "redirect:/courses/" + id;
    }
}
