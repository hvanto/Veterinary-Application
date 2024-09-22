package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.service.PrescriptionService;
import au.edu.rmit.sept.webapp.repository.PrescriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PrescriptionTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    private Pet testPet;
    private Prescription testPrescription;

    @BeforeEach
    public void setUp() {
        // Clear the repository before each test
        prescriptionRepository.deleteAll();

        // Set up test pet and prescription
        testPet = new Pet();
        testPet.setId(1L);
        testPet.setGender("Male");
        testPet.setSpecies("Dog");
        testPet.setBreed("Golden Retriever");
        testPet.setMicrochipped(true);
        testPet.setNotes("Healthy pet");

        testPrescription = new Prescription(1L, "Medication1", "10mg", "Twice a day", "2 weeks",
                LocalDate.now(), LocalDate.now().plusWeeks(2));
        prescriptionRepository.save(testPrescription);
    }

    @Test
    public void getCurrentPrescriptions_Success() throws Exception {
        // Mock current prescriptions for the test pet
        mockMvc.perform(get("/prescriptions/current")
                .param("petId", String.valueOf(testPet.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].medication", is("Medication1")))
                .andExpect(jsonPath("$[0].dosage", is("10mg")));
    }

    @Test
    public void getPrescriptionHistory_Success() throws Exception {
        // Mock prescription history for the test pet
        mockMvc.perform(get("/prescriptions/history")
                .param("petId", String.valueOf(testPet.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].medication", is("Medication1")))
                .andExpect(jsonPath("$[0].duration", is("2 weeks")));
    }

    @Test
    public void addPrescription_Success() throws Exception {
        // Create a JSON payload for adding a prescription
        String prescriptionJson = "{"
                + "\"petId\":\"1\","
                + "\"medication\":\"New Medication\","
                + "\"dosage\":\"5mg\","
                + "\"frequency\":\"Once a day\","
                + "\"duration\":\"1 week\","
                + "\"issueDate\":\"2023-09-21\","
                + "\"refillDate\":\"2023-09-28\""
                + "}";

        mockMvc.perform(post("/prescriptions/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(prescriptionJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Prescription added successfully"));
    }

    @Test
    public void editPrescription_Success() throws Exception {
        // Create a JSON payload for editing the prescription
        String editPrescriptionJson = "{"
                + "\"id\":\"" + testPrescription.getId() + "\","
                + "\"medication\":\"Updated Medication\","
                + "\"dosage\":\"15mg\","
                + "\"frequency\":\"Once a day\","
                + "\"duration\":\"3 weeks\","
                + "\"issueDate\":\"2023-09-21\","
                + "\"refillDate\":\"2023-10-12\""
                + "}";

        mockMvc.perform(put("/prescriptions/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(editPrescriptionJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Prescription updated successfully"))
                .andExpect(jsonPath("$.dosage", is("15mg")));
    }

    @Test
    public void deletePrescription_Success() throws Exception {
        // Delete the test prescription by ID
        mockMvc.perform(delete("/prescriptions/delete")
                .param("id", String.valueOf(testPrescription.getId())))
                .andExpect(status().isOk())
                .andExpect(content().string("Prescription deleted successfully"));
    }

    @Test
    public void exportToPDF_TriggersPrint() throws Exception {
        // Test if the print dialog is triggered for export to PDF
        mockMvc.perform(get("/prescriptions/export"))
                .andExpect(status().isOk())
                .andExpect(content().string("Print function triggered"));
    }
}
