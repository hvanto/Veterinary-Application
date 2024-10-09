package au.edu.rmit.sept.webapp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.repository.MedicalHistoryRepository;
import au.edu.rmit.sept.webapp.repository.VeterinarianRepository;
import au.edu.rmit.sept.webapp.service.VeterinarianService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrescriptionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VeterinarianService veterinarianService;

    private Veterinarian veterinarian;

    @BeforeAll
    public void setUp() {
        veterinarian = veterinarianService.findByEmail("drjohn@clinic.com")
                .orElseGet(() -> veterinarianService
                        .saveVeterinarian(new Veterinarian("John", "Doe", "drjohn@clinic.com", "123456789",
                                "password123")));
    }

    @Test
    public void testGetVetPets_success() throws Exception {
        // Expect non empty list of pets to be return
        // Seed data will be added if empty
        mockMvc.perform(get("/api/prescriptions/vet-pets")
                .param("vetId", Long.toString(veterinarian.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetVetPets_vetDoesNotExists() throws Exception {
        // Empty list is returned for invalid vet id
        mockMvc.perform(get("/api/prescriptions/vet-pets")
                .param("vetId", "65536"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
