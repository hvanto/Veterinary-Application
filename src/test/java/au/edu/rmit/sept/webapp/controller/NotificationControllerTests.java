package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.NotificationRepository;
import au.edu.rmit.sept.webapp.service.NotificationService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Create a unique user using UUID to ensure no overlap with other tests
        String uniqueEmail = "testuser-" + UUID.randomUUID() + "@example.com";

        testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail(uniqueEmail);
        testUser.setPassword("password123");

        // Save the user with unique email
        userService.saveUser(testUser);
    }

    @Test
    public void testGetUserNotifications() throws Exception {
        // Fetch the notifications for the unique testUser
        mockMvc.perform(get("/api/notifications?userId=" + testUser.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].message").value("Welcome to VetCare! We're excited to have you."));
    }

    @Test
    public void testMarkNotificationAsRead() throws Exception {
        // Fetch the notification ID
        Long notificationId = notificationService.getUserNotifications(testUser).get(0).getId();

        // Mark the notification as read
        mockMvc.perform(post("/api/notifications/markAsRead/" + notificationId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Notification marked as read"));
    }
}
