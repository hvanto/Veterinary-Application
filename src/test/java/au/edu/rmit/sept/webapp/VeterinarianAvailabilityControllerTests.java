package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.model.VeterinarianAvailability;
import au.edu.rmit.sept.webapp.service.VeterinarianAvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VeterinarianAvailabilityControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VeterinarianAvailabilityService veterinarianAvailabilityService;

    @BeforeEach
    public void setUp() {
        // Set up any required preconditions here
    }

    @Test
    public void getAvailabilityByVeterinarianId_Success() throws Exception {
        // Test fetching availability for a veterinarian
        mockMvc.perform(post("/api/veterinarian-availability/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty());
    }
}