package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // Insert any setup code if needed, such as seeding data into the database
    }

    @Test
    public void getUserPets_Success() throws Exception {
        // Test fetching all pets for a specific user
        mockMvc.perform(get("/api/medical-records/user-pets")
                        .param("userId", "1") // Replace with a valid user ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty());
    }

    /*
    @Test
    public void downloadMedicalRecords_PDF_Success() throws Exception {
        // Test downloading medical records as PDF
        mockMvc.perform(get("/api/medical-records/download")
                        .param("selectedPetId", "1") // Replace with valid pet ID
                        .param("format", "pdf")
                        .param("sections", "medicalHistory,physicalExam,vaccinations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/pdf"));
    }
    */

    /*
    @Test
    public void downloadMedicalRecords_XML_Success() throws Exception {
        // Test downloading medical records as XML
        mockMvc.perform(get("/api/medical-records/download")
                        .param("selectedPetId", "1") // Replace with valid pet ID
                        .param("format", "xml")
                        .param("sections", "medicalHistory,physicalExam,vaccinations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/xml"));
    }
    */

    @Test
    public void getUserPets_NoPets_SeedsData() throws Exception {
        // Test that data is seeded when no pets exist for a user
        mockMvc.perform(get("/api/medical-records/user-pets")
                        .param("userId", "2") // Replace with a user ID that has no pets
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty()); // Expect that pets are returned after seeding
    }
}
