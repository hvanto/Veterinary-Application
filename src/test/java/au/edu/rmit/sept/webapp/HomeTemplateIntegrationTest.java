package au.edu.rmit.sept.webapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeTemplateIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomePageRendersSuccessfully() throws Exception {
        // Perform a GET request to /home (the correct route in your controller)
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())  // Ensure the page loads successfully (HTTP 200 OK)
                .andExpect(content().string(containsString("Working with you for the health and happiness of your pet")))  // Check the hero section text
                .andExpect(content().string(containsString("Medical Records")))  // Check that the Medical Records card is present
                .andExpect(content().string(containsString("Prescriptions")))  // Check that the Prescriptions card is present
                .andExpect(content().string(containsString("Book & Manage Appointments")))  // Check that the Appointments card is present
                .andExpect(content().string(containsString("Educational Resources")));  // Check that the Educational Resources card is present
    }
}
