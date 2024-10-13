package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Notification;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationServiceTests {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Use a unique email for each test to avoid conflicts with existing users
        String uniqueEmail = "testuser-" + UUID.randomUUID() + "@example.com";

        // Create a user with a unique email
        testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail(uniqueEmail);  // Unique email to ensure no conflicts
        testUser.setPassword("password123");
        userService.saveUser(testUser);
    }

    @Test
    public void testCreateNotification() {
        // Create notification for the user
        List<Notification> notifications = notificationService.getUserNotifications(testUser);

        // Verify the default notification exists
        assertEquals(1, notifications.size());
        assertEquals("Welcome to VetCare! We're excited to have you.", notifications.get(0).getMessage());
    }

    @Test
    public void testGetUserNotifications() {
        // Create multiple notifications with unique messages
        notificationService.createNotification(testUser, "Notification 1");
        notificationService.createNotification(testUser, "Notification 2");

        // Retrieve notifications for the user
        List<Notification> notifications = notificationService.getUserNotifications(testUser);

        // Verify all notifications exist (including default one)
        assertEquals(3, notifications.size());
    }

    @Test
    public void testMarkNotificationAsRead() {
        // Create a notification for the user
        notificationService.createNotification(testUser, "New notification");

        // Retrieve notifications and mark one as read
        List<Notification> notifications = notificationService.getUserNotifications(testUser);
        Notification notification = notifications.get(0);
        assertFalse(notification.isRead());

        // Mark notification as read
        notificationService.markAsRead(notification.getId());

        // Verify the notification is marked as read
        Notification updatedNotification = notificationRepository.findById(notification.getId()).orElse(null);
        assertNotNull(updatedNotification);
        assertTrue(updatedNotification.isRead());
    }
}
