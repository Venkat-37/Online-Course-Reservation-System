package com.online_c.learnloop.controller;

import com.online_c.learnloop.dto.LoginRequestDto;
import com.online_c.learnloop.dto.LoginResponseDto;
import com.online_c.learnloop.dto.UserRegistrationDto;
import com.online_c.learnloop.model.User;
import com.online_c.learnloop.security.JwtUtils;
import com.online_c.learnloop.security.UserDetailsImpl;
import com.online_c.learnloop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        User user = (User)userService.registerUser(registrationDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = (User) userService.getUserByEmail(userDetails.getUsername());
        
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setToken(jwt);
        responseDto.setId(userDetails.getId());
        responseDto.setEmail(userDetails.getUsername());
        responseDto.setFullName(user.getFullName());
        responseDto.setUserType(user.getUserType());
        responseDto.setProfileCompletionPercentage(user.getProfileCompletionPercentage());
        
        return ResponseEntity.ok(responseDto);
    }
}