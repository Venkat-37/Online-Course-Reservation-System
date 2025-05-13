package com.online_c.learnloop.controller;

import com.online_c.learnloop.model.Entrollment;
import com.online_c.learnloop.security.UserDetailsImpl;
import com.online_c.learnloop.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;

    @PostMapping("/{courseId}")
    public String enrollCourse(
            @PathVariable String courseId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            RedirectAttributes redirectAttributes) {
        
        enrollmentService.enrollUserInCourse(userDetails.getId(), courseId);
        redirectAttributes.addAttribute("enrolled", true);
        return "redirect:/courses/" + courseId;
    }
    
    @PostMapping("/{enrollmentId}/progress")
    public String updateEnrollmentProgress(
            @PathVariable String enrollmentId,
            @RequestParam int progressPercentage,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            enrollmentService.updateEnrollmentProgress(enrollmentId, userDetails.getId(), progressPercentage);
            redirectAttributes.addFlashAttribute("successMessage", "Progress updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile/enrollments";
    }
}
