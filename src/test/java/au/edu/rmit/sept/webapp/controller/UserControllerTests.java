package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // No need for deleteAll()

    @Test
    public void signup_Success() throws Exception {
        // Generate a unique email to ensure no conflicts
        String uniqueEmail = "john.doe-" + UUID.randomUUID() + "@example.com";

        // Create a JSON payload for the signup request with a unique email
        String userJson = "{"
                + "\"firstName\":\"John\","
                + "\"lastName\":\"Doe\","
                + "\"email\":\"" + uniqueEmail + "\","
                + "\"contact\":\"1234567890\","
                + "\"password\":\"password123\""
                + "}";

        // Perform the signup request and expect a success response
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andDo(print()) // Print the request and response
                .andExpect(status().isOk())
                .andExpect(content().string("User signed up successfully!"));
    }

    @Test
    public void signup_EmailAlreadyExists() throws Exception {
        // Add a user to the database to test email existence check
        String uniqueEmail = "jane.doe-" + UUID.randomUUID() + "@example.com";
        User existingUser = new User();
        existingUser.setFirstName("Jane");
        existingUser.setLastName("Doe");
        existingUser.setEmail(uniqueEmail);  // Unique email for this test
        existingUser.setPassword("password123");
        userService.saveUser(existingUser);

        // Create a JSON payload with the same email
        String userJson = "{"
                + "\"firstName\":\"John\","
                + "\"lastName\":\"Doe\","
                + "\"email\":\"" + uniqueEmail + "\","
                + "\"contact\":\"1234567890\","
                + "\"password\":\"password123\""
                + "}";

        // Perform the signup request and expect an email already exists error
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andDo(print()) // Print the request and response
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));
    }

    @Test
    public void login_IncorrectPassword() throws Exception {
        // Setup: Create and save a user with a unique email
        String uniqueEmail = "jane.doe-" + UUID.randomUUID() + "@example.com";
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail(uniqueEmail);  // Unique email for this test
        user.setPassword("password123");
        userService.saveUser(user);

        // Create JSON payload with incorrect password
        String loginJson = "{"
                + "\"email\":\"" + uniqueEmail + "\","
                + "\"password\":\"wrongpassword\""
                + "}";

        // Perform login request and expect failure
        mockMvc.perform(post("/api/users/loginUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andDo(print()) // Print the request and response
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    public void login_Success() throws Exception {
        // Setup: Create and save a user with a unique email
        String uniqueEmail = "john.doe-" + UUID.randomUUID() + "@example.com";
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail(uniqueEmail);  // Unique email for this test
        user.setPassword("password123");
        user.setContact("1234567890");
        user.setImage("default_profile.png");
        userService.saveUser(user);

        // Create JSON payload for login
        String loginJson = "{"
                + "\"email\":\"" + uniqueEmail + "\","
                + "\"password\":\"password123\""
                + "}";

        // Perform login request and expect success with dynamic field handling
        mockMvc.perform(post("/api/users/loginUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andDo(print()) // Print the request and response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value(uniqueEmail))
                .andExpect(jsonPath("$.password").exists()) // Checks if password field exists
                .andExpect(jsonPath("$.password").value(org.hamcrest.Matchers.matchesPattern("^\\$2a\\$10\\$.*"))) // Check that it's a bcrypt hash
                .andExpect(jsonPath("$.createdOn").exists()) // Check that createdOn exists
                .andExpect(jsonPath("$.updatedOn").exists()) // Check that updatedOn exists
                .andExpect(jsonPath("$.deleted").value(false));
    }

    @Test
    public void login_EmailNotFound() throws Exception {
        // Create a JSON payload with a non-existent email
        String loginJson = "{"
                + "\"email\":\"nonexistent-" + UUID.randomUUID() + "@example.com\","
                + "\"password\":\"password123\""
                + "}";

        // Perform login request and expect failure
        mockMvc.perform(post("/api/users/loginUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andDo(print()) // Print the request and response
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    public void updateAccountDetails_Success() throws Exception {
        // Setup: Create and save a user with a unique email
        String uniqueEmail = "john.doe-" + UUID.randomUUID() + "@example.com";
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail(uniqueEmail);  // Unique email for this test
        user.setPassword("password123");
        user.setContact("1234567890");
        userService.saveUser(user);

        // Generate another UUID for the updated email
        String newUniqueEmail = "johnny.doe-" + UUID.randomUUID() + "@example.com";

        // Create JSON payload for updating account details
        String updateJson = "{"
                + "\"id\":\"" + user.getId() + "\","
                + "\"firstName\":\"Johnny\","
                + "\"lastName\":\"Doey\","
                + "\"email\":\"" + newUniqueEmail + "\","
                + "\"contact\":\"0987654321\""
                + "}";

        // Perform update account details request and expect success
        mockMvc.perform(put("/api/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andDo(print()) // Print the request and response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Johnny")))
                .andExpect(jsonPath("$.lastName", is("Doey")))
                .andExpect(jsonPath("$.email", is(newUniqueEmail)))  // Use the same unique email for assertion
                .andExpect(jsonPath("$.contact", is("0987654321")));
    }

    @Test
    public void updatePassword_Success() throws Exception {
        // Setup: Create and save a user with a unique email
        String uniqueEmail = "john.doe-" + UUID.randomUUID() + "@example.com";
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail(uniqueEmail);  // Unique email for this test
        user.setPassword("password123");
        userService.saveUser(user);

        // Create JSON payload for updating password
        String updatePasswordJson = "{"
                + "\"id\":\"" + user.getId() + "\","
                + "\"password\":\"newpassword123\""
                + "}";

        // Perform update password request and expect success
        mockMvc.perform(put("/api/users/updatePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePasswordJson))
                .andDo(print()) // Print the request and response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Password updated successfully!")));
    }
}