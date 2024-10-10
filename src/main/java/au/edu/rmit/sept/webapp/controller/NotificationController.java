package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Notification;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.NotificationService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    /**
     * Get notifications for a specific user.
     *
     * @param userId the ID of the user whose notifications we want to retrieve
     * @return List of notifications for the user
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications(@RequestParam Long userId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        List<Notification> notifications = notificationService.getUserNotifications(user);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Marks a notification as read.
     *
     * @param id the ID of the notification to mark as read
     * @return Success message
     */
    @PostMapping("/markAsRead/{id}")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Notification marked as read");
    }

    /**
     * Creates a new notification for a user and broadcasts it via WebSocket.
     *
     * @param userId  the ID of the user to send the notification to
     * @param message the notification message
     * @return Success message
     */
    @PostMapping("/create")
    public ResponseEntity<String> createNotification(@RequestParam Long userId, @RequestBody String message) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        notificationService.createNotification(user, message);

        // Send the notification via WebSocket
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);

        return ResponseEntity.status(HttpStatus.CREATED).body("Notification created and sent");
    }
}
