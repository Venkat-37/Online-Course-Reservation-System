package com.online_c.learnloop.service;

import com.online_c.learnloop.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${file.upload-dir:./uploads}")
    private String uploadDirectory;
    
    private final UserService userService;
    
    public String storeProfilePhoto(MultipartFile file, String userEmail) {
        try {
            // Create directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate unique filename
            String filename = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
            Path filePath = uploadPath.resolve(filename);
            
            // Copy file to the target location
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Update user profile with the photo URL
            String fileUrl = "/uploads/" + filename;
            User user = userService.getUserByEmail(userEmail);
            user.setProfilePhotoUrl(fileUrl);
            user.calculateProfileCompletionPercentage();
            userService.updateUser(user);
            
            return fileUrl;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file", ex);
        }
    }
    
    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }
}
