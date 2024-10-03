package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.EncryptionService;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testSaveUser_WithEncryption() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        // Save the user and verify that the password is encrypted
        User savedUser = userService.saveUser(user);
        assertNotEquals("password123", savedUser.getPassword());
        assertTrue(encryptionService.matchesPassword("password123", savedUser.getPassword()));
    }

    @Test
    public void testEmailExists() {
        // Add a user to test the email existence check
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.com");
        user.setPassword("password123");
        userService.saveUser(user);

        // Check if the email exists in the database
        assertTrue(userService.emailExists("jane.doe@example.com"));
        assertFalse(userService.emailExists("john.doe@example.com"));
    }

    @Test
    public void testValidateUserCredentials_Success() throws Exception {
        // Setup: Create and save a user
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        userService.saveUser(user);

        // Validate credentials with correct email and password
        User validatedUser = userService.validateUserCredentials("john.doe@example.com", "password123");
        assertNotNull(validatedUser);
        assertEquals("John", validatedUser.getFirstName());
    }

    @Test
    public void testValidateUserCredentials_IncorrectPassword() {
        // Setup: Create and save a user
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.com");
        user.setPassword("password123");
        userService.saveUser(user);

        // Attempt to validate credentials with incorrect password
        Exception exception = assertThrows(Exception.class, () -> {
            userService.validateUserCredentials("jane.doe@example.com", "wrongpassword");
        });

        // Check that the error message is as expected
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    public void testValidateUserCredentials_EmailNotFound() {
        // Attempt to validate credentials with a non-existent email
        Exception exception = assertThrows(Exception.class, () -> {
            userService.validateUserCredentials("nonexistent@example.com", "password123");
        });

        // Check that the error message is as expected
        assertEquals("Email not found", exception.getMessage());
    }
}
