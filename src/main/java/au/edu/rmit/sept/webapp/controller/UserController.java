package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

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
