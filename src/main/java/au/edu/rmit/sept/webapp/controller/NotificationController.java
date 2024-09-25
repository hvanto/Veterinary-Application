package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Notification;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.NotificationService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications(Principal principal) {
        User user = userService.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        List<Notification> notifications = notificationService.getUserNotifications(user);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/markAsRead/{id}")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Notification marked as read");
    }
}
