package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.nio.file.Path;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Handle signup
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signup(@RequestBody User user) {
        try {
            // Check if email already exists
            if (userService.emailExists(user.getEmail())) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            // Save user to the database
            userService.saveUser(user);
            return ResponseEntity.ok("User signed up successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Handle login
    @PostMapping(value = "/loginUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> login(@RequestBody User loginRequest) {
        try {
            // Validate user credentials and return user if successful
            User user = userService.validateUserCredentials(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Update user details in the database
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        try {
            // Update user details in the database
            User user = userService.updateUser(updatedUser);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Handle password update
    @PutMapping(value = "/updatePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.updatePassword(user.getId(), user.getPassword());
            response.put("message", "Password updated successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to update password: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Update user profile and handle image upload
    @PostMapping("/updateProfile")
    public ResponseEntity<Map<String, String>> updateProfile(@RequestParam("userId") Long userId, 
                                                             @RequestParam(value = "image", required = false) MultipartFile image,
                                                             @RequestParam("firstName") String firstName,
                                                             @RequestParam("lastName") String lastName,
                                                             @RequestParam("contact") String contact) {
        Map<String, String> response = new HashMap<>();
    
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            response.put("error", "User not found");
            return ResponseEntity.badRequest().body(response);
        }
    
        User user = userOptional.get();
        
        // If an image is uploaded, save it
        if (image != null && !image.isEmpty()) {
            try {
                String imagePath = saveImage(image);
                user.setImage(imagePath);
            } catch (IOException e) {
                response.put("error", "Failed to save image.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }

        // Call the new update method in UserService to handle updating user details and notifications
        userService.updateUserDetails(user, firstName, lastName, contact);

        response.put("message", "Profile updated successfully!");
        return ResponseEntity.ok(response);
    }

    // The same saveImage method you have for pets can be reused here
    private String saveImage(MultipartFile image) throws IOException {
        String uploadDir = "src/main/resources/static/assets";
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadDir, fileName);
        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return "/assets/profile_pics/" + fileName;
    }

    // Update completed guide status in the database
    @PutMapping(value = "/updateCompletedGuide", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> updateCompletedGuide(@RequestBody Map<String, Object> requestBody) {
        Map<String, String> response = new HashMap<>();
        try {
            // Extract the user id and completed guide status from the request body
            Long userId = Long.parseLong(requestBody.get("userId").toString());
            boolean completedGuide = Boolean.parseBoolean(requestBody.get("completedGuide").toString());

            userService.updateCompletedGuide(userId, completedGuide);

            response.put("message", "Completed guide status updated successfully.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", "Failed to update completed guide status: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
