package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.service.VeterinarianService;
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
public class VeterinarianControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VeterinarianService veterinarianService;

    @BeforeEach
    public void setUp() {
        // Set up any required preconditions here
    }

    @Test
    public void getAllVeterinarians_Success() throws Exception {
        // Test fetching all veterinarians
        mockMvc.perform(post("/api/veterinarian/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty());
    }

    @Test
    public void getVeterinariansByClinic_Success() throws Exception {
        // Test fetching veterinarians by clinic ID
        mockMvc.perform(post("/api/veterinarian/clinic/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getVeterinariansByService_Success() throws Exception {
        // Test fetching veterinarians by service ID
        mockMvc.perform(post("/api/veterinarian/service/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}