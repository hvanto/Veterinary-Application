package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // Clear the repository before each test to prevent duplicate entry errors
        userRepository.deleteAll();
    }

    @Test
    public void signup_Success() throws Exception {
        // Create a JSON payload for the signup request
        String userJson = "{"
                + "\"firstName\":\"John\","
                + "\"lastName\":\"Doe\","
                + "\"email\":\"john.doe@example.com\","
                + "\"contact\":\"1234567890\","
                + "\"password\":\"password123\""
                + "}";

        // Perform the signup request and expect a success response
        mockMvc.perform(post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string("User signed up successfully!"));
    }

    @Test
    public void signup_EmailAlreadyExists() throws Exception {
        // Add a user to the database to test email existence check
        User existingUser = new User();
        existingUser.setFirstName("Jane");
        existingUser.setLastName("Doe");
        existingUser.setEmail("jane.doe@example.com");
        existingUser.setPassword("password123");
        userService.saveUser(existingUser);

        // Create a JSON payload with the same email
        String userJson = "{"
                + "\"firstName\":\"John\","
                + "\"lastName\":\"Doe\","
                + "\"email\":\"jane.doe@example.com\","
                + "\"contact\":\"1234567890\","
                + "\"password\":\"password123\""
                + "}";

        // Perform the signup request and expect an email already exists error
        mockMvc.perform(post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));
    }
    

    @Test
    public void login_IncorrectPassword() throws Exception {
        // Setup: Create and save a user
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.com");
        user.setPassword("password123");
        userService.saveUser(user);

        // Create JSON payload with incorrect password
        String loginJson = "{"
                + "\"email\":\"jane.doe@example.com\","
                + "\"password\":\"wrongpassword\""
                + "}";

        // Perform login request and expect failure
        mockMvc.perform(post("/api/users/loginUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    public void login_Success() throws Exception {
        // Setup: Create and save a user
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123"); // Assuming the saveUser method handles encryption
        user.setContact("1234567890");
        user.setImage("default_profile.png");
        userService.saveUser(user);
    
        // Create JSON payload for login
        String loginJson = "{"
                + "\"email\":\"john.doe@example.com\","
                + "\"password\":\"password123\""
                + "}";
    
        // Perform login request and expect success with dynamic field handling
        mockMvc.perform(post("/api/users/loginUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.password").exists()) // Checks if password field exists
                .andExpect(jsonPath("$.password").value(org.hamcrest.Matchers.matchesPattern("^\\$2a\\$10\\$.*"))) // Check that it's a bcrypt hash
                .andExpect(jsonPath("$.createdOn").exists()) // Check that createdOn exists
                .andExpect(jsonPath("$.updatedOn").exists()) // Check that updatedOn exists
                .andExpect(jsonPath("$.deleted").value(false));
    }
    


    @Test
    public void login_EmailNotFound() throws Exception {
        // Create JSON payload with a non-existent email
        String loginJson = "{"
                + "\"email\":\"nonexistent@example.com\","
                + "\"password\":\"password123\""
                + "}";

        // Perform login request and expect failure
        mockMvc.perform(post("/api/users/loginUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    public void updateAccountDetails_Success() throws Exception {
        // Setup: Create and save a user
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setContact("1234567890");
        userService.saveUser(user);

        // Create JSON payload for updating account details
        String updateJson = "{"
                + "\"id\":\"" + user.getId() + "\","
                + "\"firstName\":\"Johnny\","
                + "\"lastName\":\"Doey\","
                + "\"email\":\"johnny.doe@example.com\","
                + "\"contact\":\"0987654321\""
                + "}";

        // Perform update account details request and expect success
        mockMvc.perform(put("/api/users/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Johnny")))
                .andExpect(jsonPath("$.lastName", is("Doey")))
                .andExpect(jsonPath("$.email", is("johnny.doe@example.com")))
                .andExpect(jsonPath("$.contact", is("0987654321")));
    }

    @Test
    public void updatePassword_Success() throws Exception {
        // Setup: Create and save a user
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Password updated successfully!")));
    }
}