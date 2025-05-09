package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testFindByEmail() {
        // Generate a unique identifier (e.g., current timestamp) to ensure unique email addresses
        String uniqueIdentifier = String.valueOf(System.currentTimeMillis());

        // Create and save a user
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe" + uniqueIdentifier + "@example.com");
        user.setPassword("password123");
        userRepository.save(user);

        // Find the user by email
        Optional<User> foundUser = userRepository.findByEmail("john.doe" + uniqueIdentifier +"@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("John", foundUser.get().getFirstName());
    }

    @Test
    public void testExistsByEmail() {
        // Generate a unique identifier (e.g., current timestamp) to ensure unique email addresses
        String uniqueIdentifier = String.valueOf(System.currentTimeMillis());

        // Create and save a user
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane.doe" + uniqueIdentifier + "@example.com");
        user.setPassword("password123");
        userRepository.save(user);

        // Check if the email exists in the repository
        assertTrue(userRepository.existsByEmail("jane.doe" + uniqueIdentifier + "@example.com"));
        assertFalse(userRepository.existsByEmail("john.doe" + uniqueIdentifier + "@example.com"));
    }
}