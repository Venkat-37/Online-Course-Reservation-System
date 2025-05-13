package com.online_c.learnloop.controller;

import com.online_c.learnloop.dto.UserProfileDto;
import com.online_c.learnloop.model.Entrollment;
import com.online_c.learnloop.model.User;
import com.online_c.learnloop.security.UserDetailsImpl;
import com.online_c.learnloop.service.EnrollmentService;
import com.online_c.learnloop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final EnrollmentService enrollmentService;

    @GetMapping
    public String viewProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        UserProfileDto profileDto = userService.mapToProfileDto(user);
        model.addAttribute("profile", profileDto);
        return "profile/view";
    }

    @GetMapping("/edit")
    public String editProfileForm(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        UserProfileDto profileDto = userService.mapToProfileDto(user);
        model.addAttribute("profile", profileDto);
        return "profile/edit";
    }

    @PostMapping("/edit")
    public String updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                               @Valid @ModelAttribute("profile") UserProfileDto profileDto,
                               @RequestParam(required = false) String interestsString,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "profile/edit";
        }
        
        // Process interests from comma-separated string
        if (interestsString != null && !interestsString.trim().isEmpty()) {
            Set<String> interests = Arrays.stream(interestsString.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toSet());
            profileDto.setInterests(interests);
        } else {
            profileDto.setInterests(new HashSet<>());
        }
        
        userService.updateUserProfile(userDetails.getUsername(), profileDto);
        return "redirect:/profile?updated";
    }

    @GetMapping("/enrollments")
    public String viewEnrollments(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<Entrollment> enrollments = enrollmentService.getUserEnrollments(userDetails.getId());
        model.addAttribute("enrollments", enrollments);
        return "profile/enrollments";
    }
    
    @PostMapping("/enrollments/{id}/progress")
    public String updateEnrollmentProgress(@PathVariable String id,
                                        @RequestParam int progressPercentage,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        enrollmentService.updateEnrollmentProgress(id, userDetails.getId(), progressPercentage);
        return "redirect:/profile/enrollments";
    }
}
