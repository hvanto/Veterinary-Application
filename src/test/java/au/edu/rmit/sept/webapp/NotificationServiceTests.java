package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.model.Notification;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.NotificationRepository;
import au.edu.rmit.sept.webapp.service.NotificationService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        notificationRepository.deleteAll();
        userService.deleteAllUsers();

        testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");
        userService.saveUser(testUser);
    }

    @AfterEach
    public void tearDown() {
        notificationRepository.deleteAll();
        userService.deleteAllUsers();
    }

    @Test
    public void testCreateNotification() {

        List<Notification> notifications = notificationService.getUserNotifications(testUser);
        assertEquals(1, notifications.size()); 
        assertEquals("Welcome to VetCare! We're excited to have you.", notifications.get(0).getMessage());
    }

    @Test
    public void testGetUserNotifications() {
        notificationService.createNotification(testUser, "Notification 1");
        notificationService.createNotification(testUser, "Notification 2");

        List<Notification> notifications = notificationService.getUserNotifications(testUser);
        assertEquals(3, notifications.size());
    }

    @Test
    public void testMarkNotificationAsRead() {
        notificationService.createNotification(testUser, "New notification");
        List<Notification> notifications = notificationService.getUserNotifications(testUser);

        Notification notification = notifications.get(0);
        assertFalse(notification.isRead());

        notificationService.markAsRead(notification.getId());

        Notification updatedNotification = notificationRepository.findById(notification.getId()).orElse(null);
        assertNotNull(updatedNotification);
        assertTrue(updatedNotification.isRead());
    }
}
