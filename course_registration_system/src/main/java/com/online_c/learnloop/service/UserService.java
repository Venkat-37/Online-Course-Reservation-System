package com.online_c.learnloop.service;

import com.online_c.learnloop.dto.UserProfileDto;
import com.online_c.learnloop.dto.UserRegistrationDto;
import com.online_c.learnloop.model.User;
import com.online_c.learnloop.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new EntityExistsException("Email already in use");
        }

        User user = new User();
        user.setFullName(registrationDto.getFullName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setPhone(registrationDto.getPhone());
        user.calculateProfileCompletionPercentage();
        
        return userRepository.save(user);
    }

    public User getUserById(String id) {
        return (User) userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User getUserByEmail(String email) {
        return (User) userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    public UserProfileDto mapToProfileDto(User user) {
        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setFullName(user.getFullName());
        profileDto.setEmail(user.getEmail());
        profileDto.setPhone(user.getPhone());
        profileDto.setDateOfBirth(user.getDateOfBirth());
        profileDto.setEducation(user.getEducation());
        profileDto.setProfession(user.getProfession());
        profileDto.setInterests(user.getInterests());
        profileDto.setBio(user.getBio());
        profileDto.setLinkedinProfile(user.getLinkedinProfile());
        profileDto.setGithubProfile(user.getGithubProfile());
        profileDto.setPersonalWebsite(user.getPersonalWebsite());
        profileDto.setLocation(user.getLocation());
        profileDto.setProfileCompletionPercentage(user.getProfileCompletionPercentage());
        return profileDto;
    }
    
    public User updateUserProfile(String email, UserProfileDto profileDto) {
        User user = getUserByEmail(email);
        
        if (profileDto.getFullName() != null) user.setFullName(profileDto.getFullName());
        if (profileDto.getPhone() != null) user.setPhone(profileDto.getPhone());
        if (profileDto.getDateOfBirth() != null) user.setDateOfBirth(profileDto.getDateOfBirth());
        if (profileDto.getEducation() != null) user.setEducation(profileDto.getEducation());
        if (profileDto.getProfession() != null) user.setProfession(profileDto.getProfession());
        if (profileDto.getInterests() != null) user.setInterests(profileDto.getInterests());
        if (profileDto.getBio() != null) user.setBio(profileDto.getBio());
        if (profileDto.getLinkedinProfile() != null) user.setLinkedinProfile(profileDto.getLinkedinProfile());
        if (profileDto.getGithubProfile() != null) user.setGithubProfile(profileDto.getGithubProfile());
        if (profileDto.getPersonalWebsite() != null) user.setPersonalWebsite(profileDto.getPersonalWebsite());
        if (profileDto.getLocation() != null) user.setLocation(profileDto.getLocation());
        
        user.calculateProfileCompletionPercentage();
        return userRepository.save(user);
    }
}