package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.repository.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.VeterinarianService;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrescriptionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @Autowired
    private VeterinarianService veterinarianService;

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private PrescriptionHistoryRepository prescriptionHistoryRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private RefillRepository refillRepository;

    private Veterinarian veterinarian;
    private Pet pet;

    private Prescription prescription;

    private String json;

    @BeforeAll
    public void setUp() {
        // Use unique email and veterinarian name for this test setup
        veterinarian = veterinarianService.findByEmail("drjohn-" + UUID.randomUUID() + "@clinic.com")
                .orElseGet(() -> veterinarianService.saveVeterinarian(new Veterinarian("John", "Doe",
                        "drjohn-" + UUID.randomUUID() + "@clinic.com", "123456789", "password123")));

        User user = userService.findByEmail("jane.doe-" + UUID.randomUUID() + "@example.com")
                .orElseGet(() -> userService.saveUser(new User("Jane", "Doe",
                        "jane.doe-" + UUID.randomUUID() + "@example.com", "password123", "123456789")));

        List<Pet> pets = petService.getPetsByUserId(user.getId());

        if (pets.isEmpty()) {
            pet = new Pet(user, "Buddy-" + UUID.randomUUID(), "Dog", "Golden Retriever", "Male", true,
                    "Loves to play fetch", "2_buddy_retriever.png", LocalDate.of(2018, 1, 5));
            petService.save(pet);
        } else {
            pet = petService.getPetsByUserId(user.getId()).get(0);
        }

        json = "{\n" +
        "    \"id\": " + pet.getId() + ",\n" +
        "    \"pet\": {\n" +
        "        \"id\": 1,\n" +
        "        \"name\": \"Buddy-" + UUID.randomUUID() + "\",\n" +
        "        \"gender\": \"Male\",\n" +
        "        \"species\": \"Dog\",\n" +
        "        \"breed\": \"Golden Retriever\",\n" +
        "        \"microchipped\": true,\n" +
        "        \"notes\": \"Loves to play fetch\",\n" +
        "        \"imagePath\": \"2_buddy_retriever.png\",\n" +
        "        \"dateOfBirth\": \"2018-01-04\",\n" +
        "        \"appointments\": [],\n" +
        "        \"age\": 6\n" +
        "    },\n" +
        "    \"practitioner\": \"Dr. John Doe\",\n" +
        "    \"prescription\": \"Amoxicillin\",\n" +
        "    \"vet\": \"fdsa\",\n" +
        "    \"dosage\": \"250mg twice a day\",\n" +
        "    \"startDate\": 1727568000000,\n" +
        "    \"endDate\": 1729296000000,\n" +
        "    \"description\": \"For bacterial infection\",\n" +
        "    \"cost\": 2.3\n" +
        "}";

        prescription = new Prescription(
                pet,
                "Amoxicillin-" + UUID.randomUUID(),
                "Dr. John Doe" + UUID.randomUUID(),
                "250mg twice a day",
                new Date(1727568000000L),
                new Date(1729296000000L),
                "For bacterial infection",
                2.3);
    }

    @BeforeEach
    public void prepare() {
        // wipe table to remove variables
        refillRepository.deleteAll();
        prescriptionRepository.deleteAll();
    }
//
//    @AfterAll
//    public void teardown() {
//        medicalHistoryRepository.deleteAll();
//        refillRepository.deleteAll();
//        prescriptionRepository.deleteAll();
//        prescriptionHistoryRepository.deleteAll();
//        petRepository.deleteAll();
//    }

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


    @Test
    public void testAddPrescription_success() throws Exception {
        // call handler with mock mvc
        mockMvc.perform(post("/api/prescriptions/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void testUpdatePrescription_success() throws Exception {
        // Save prescription with unique identifier
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        savedPrescription.setPractitioner("Dr. Null");
        // Update the prescription
        mockMvc.perform(put("/api/prescriptions/" + savedPrescription.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        // Practitioner should be updated
        assertNotEquals("Dr. Null", prescriptionRepository.findById(savedPrescription.getId()).get().getPractitioner());
    }
    
//    @Test
//    public void testUpdatePrescription_notFound() throws Exception {
//        // call handler with mock mvc
//        // expect a not found response
//        mockMvc.perform(delete("/api/prescriptions/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isNotFound())
//                .andReturn();
//    }

    @Test
    public void testDeletePrescription_success() throws Exception {
        // add test prescription
        Prescription saved = prescriptionRepository.save(prescription);

        // call handler with mock mvc
        mockMvc.perform(delete("/api/prescriptions/" + saved.getId()))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testDeletePrescription_notFound() throws Exception {
        // call handler with mock mvc
        // expect a not found response
        mockMvc.perform(delete("/api/prescriptions/9999"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testAddPrescription_invalidData() throws Exception {
        // Pass an empty prescription JSON to simulate invalid data
        String invalidJson = "{\n" +
                "    \"pet\": " + pet + ",\n" +  // Include pet ID
                "    \"practitioner\": \"\",\n" +  // Empty fields to trigger bad request
                "    \"prescription\": \"\",\n" +
                "    \"dosage\": \"\",\n" +
                "    \"startDate\": null,\n" +
                "    \"endDate\": null,\n" +
                "    \"description\": \"\",\n" +
                "    \"cost\": 0\n" +
                "}";

        mockMvc.perform(post("/api/prescriptions/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testGetAllPrescriptions_success() throws Exception {
        // Add a test prescription to the database
        prescriptionRepository.save(prescription);

        // Fetch all prescriptions using a request parameter for petId
        mockMvc.perform(get("/api/prescriptions/all")
                        .param("petId", pet.getId().toString())) // Use param instead of path variable
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }


    @Test
    public void testGetAllPrescriptions_empty() throws Exception {
        // Fetch all prescriptions when none exist
        mockMvc.perform(get("/api/prescriptions/all/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetPrescriptionById_success() throws Exception {
        // Add a test prescription to the database
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        // Fetch the prescription by ID
        mockMvc.perform(get("/api/prescriptions/" + savedPrescription.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedPrescription.getId()));
    }

    @Test
    public void testGetPrescriptionById_notFound() throws Exception {
        // Fetch a prescription by non-existing ID
        mockMvc.perform(get("/api/prescriptions/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddRefill_success() throws Exception {
        Prescription saved = prescriptionRepository.save(prescription);
        // Refill request JSON data (sample)
        String refillJson = "{\n" +
                "    \"firstName\": \"Jane\",\n" +
                "    \"lastName\": \"Doe\",\n" +
                "    \"userPhone\": \"123456789\",\n" +
                "    \"address\": \"123 Main St\",\n" +
                "    \"creditCardNumber\": \"4111111111111111\",\n" +
                "    \"cost\": 2.3,\n" +
                "    \"prescription\": { \"id\": " + saved.getId() + " },\n" +  // Prescription object with ID
                "    \"userId\": 1,\n" +
                "    \"expiryDate\": \"12/25\",\n" +
                "    \"cvv\": \"123\",\n" +
                "    \"submissionDate\": \"2023-10-12\",\n" +
                "    \"recurring\": \"Weekly\" \n" +
                "}";
        // Call handler with mock mvc to add refill
        mockMvc.perform(post("/api/prescriptions/refills/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refillJson))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testAddRefill_invalidData() throws Exception {
        // Pass an invalid refill JSON (missing fields)
        String invalidRefillJson = "{}";

        mockMvc.perform(post("/api/prescriptions/refills/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRefillJson))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testGetRefillsByUserId_success() throws Exception {
        Prescription saved = prescriptionRepository.save(prescription);
        // Refill request JSON data (sample)
        String refillJson = "{\n" +
                "    \"firstName\": \"Jane\",\n" +
                "    \"lastName\": \"Doe\",\n" +
                "    \"userPhone\": \"123456789\",\n" +
                "    \"address\": \"123 Main St\",\n" +
                "    \"creditCardNumber\": \"4111111111111111\",\n" +
                "    \"cost\": 2.3,\n" +
                "    \"prescription\": { \"id\": " + saved.getId() + " },\n" +  // Prescription object with ID
                "    \"userId\": 1,\n" +
                "    \"expiryDate\": \"12/25\",\n" +
                "    \"cvv\": \"123\",\n" +
                "    \"submissionDate\": \"2023-10-12\",\n" +
                "    \"recurring\": \"Weekly\" \n" +
                "}";
        MvcResult result = mockMvc.perform(post("/api/prescriptions/refills/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refillJson))
                .andExpect(status().isOk())
                .andReturn();

        // Get the refill ID from the response body
        String responseBody = result.getResponse().getContentAsString();
        String userId = responseBody.replaceAll(".*\"userId\":\\s*(\\d+).*", "$1");

        // Fetch refills by user ID
        mockMvc.perform(get("/api/prescriptions/refills/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetRefillsByUserId_notFound() throws Exception {
        // Fetch refills by non-existing user ID
        mockMvc.perform(get("/api/prescriptions/refills/user/999"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testDeleteRefill_success() throws Exception {
        Prescription saved = prescriptionRepository.save(prescription);
        // Refill request JSON data (sample)
        String refillJson = "{\n" +
                "    \"firstName\": \"Jane\",\n" +
                "    \"lastName\": \"Doe\",\n" +
                "    \"userPhone\": \"123456789\",\n" +
                "    \"address\": \"123 Main St\",\n" +
                "    \"creditCardNumber\": \"4111111111111111\",\n" +
                "    \"cost\": 2.3,\n" +
                "    \"prescription\": { \"id\": " + saved.getId() + " },\n" +  // Prescription object with ID
                "    \"userId\": 1,\n" +
                "    \"expiryDate\": \"12/25\",\n" +
                "    \"cvv\": \"123\",\n" +
                "    \"submissionDate\": \"2023-10-12\",\n" +
                "    \"recurring\": \"Weekly\" \n" +
                "}";
        MvcResult result = mockMvc.perform(post("/api/prescriptions/refills/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refillJson))
                .andExpect(status().isOk())
                .andReturn();

        // Get the refill ID from the response body
        String responseBody = result.getResponse().getContentAsString();
        String refillId = responseBody.substring(responseBody.indexOf("\"id\":") + 5, responseBody.indexOf(",", responseBody.indexOf("\"id\":")));

        // Get the refill ID from the response and delete it
        mockMvc.perform(delete("/api/prescriptions/refills/" + refillId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteRefill_notFound() throws Exception {
        // Try to delete a non-existing refill
        mockMvc.perform(delete("/api/prescriptions/refills/999"))
                .andExpect(status().isNotFound());
    }

}
