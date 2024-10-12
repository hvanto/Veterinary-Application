package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.service.VeterinarianService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class VeterinarianControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VeterinarianService veterinarianService;

    private Veterinarian vet;

    @BeforeEach
    public void setUp() {
        // Set up any required preconditions here
        vet = new Veterinarian();
        vet.setFirstName("John");
        vet.setLastName("Doe");
        vet.setEmail("vet1@test.com");
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

    @Test
    public void getVeterinariansByClinicAndService_Success() throws Exception {
        mockMvc.perform(post("/api/veterinarian/clinic/1/service/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void signup_Success() throws Exception {
        Map<String, Object> signupData = new HashMap<>();
        signupData.put("email", "vet1@test.com");
        signupData.put("firstName", "John");
        signupData.put("lastName", "Doe");
        signupData.put("password", "password123");
        signupData.put("clinicName", "Independent");

        // Mock service behavior
        Mockito.when(veterinarianService.emailExists(anyString())).thenReturn(false);
        Mockito.when(veterinarianService.saveVeterinarian(any(Veterinarian.class)))
                .thenReturn(new Veterinarian());

        mockMvc.perform(post("/api/veterinarian/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Veterinarian signed up successfully!")));
    }

    @Test
    public void signup_EmailAlreadyExists() throws Exception {
        Map<String, Object> signupData = new HashMap<>();
        signupData.put("email", "vet2@test.com");
        signupData.put("firstName", "Jane");
        signupData.put("lastName", "Doe");
        signupData.put("password", "password123");
        signupData.put("clinicName", "Independent");

        // Mock service behavior
        Mockito.when(veterinarianService.emailExists(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/veterinarian/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupData)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Email already exists")));
    }

    @Test
    public void updatePassword_Success() throws Exception {
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setId(1L);
        veterinarian.setPassword("newPassword123");

        Mockito.doNothing().when(veterinarianService).updatePassword(1L, "newPassword123");

        mockMvc.perform(put("/api/veterinarian/updatePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veterinarian)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Password updated successfully!")));
    }

    @Test
    public void getAppointmentsByVeterinarian_Success() throws Exception {
        mockMvc.perform(post("/api/veterinarian/1/appointments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
