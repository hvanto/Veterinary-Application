package au.edu.rmit.sept.webapp.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.repository.MedicalHistoryRepository;
import au.edu.rmit.sept.webapp.repository.PetRepository;
import au.edu.rmit.sept.webapp.repository.PrescriptionHistoryRepository;
import au.edu.rmit.sept.webapp.repository.PrescriptionRepository;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.VeterinarianService;

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

        // Set JSON data using unique identifiers
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

        // Create a new unique prescription
        prescription = new Prescription(
                pet,
                "Amoxicillin-" + UUID.randomUUID(),
                "Dr. John Doe",
                "250mg twice a day",
                new Date(1727568000000L),
                new Date(1729296000000L),
                "For bacterial infection",
                2.3);
    }

    @Test
    public void testGetVetPets_success() throws Exception {
        mockMvc.perform(get("/api/prescriptions/vet-pets")
                        .param("vetId", Long.toString(veterinarian.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetVetPets_vetDoesNotExists() throws Exception {
        mockMvc.perform(get("/api/prescriptions/vet-pets")
                        .param("vetId", "65536"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

//    @Test
//    public void testAddPrescription_success() throws Exception {
//        mockMvc.perform(post("/api/prescriptions/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        assertEquals(1, prescriptionRepository.findByPet(pet).size());
//    }

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

    @Test
    public void testDeletePrescription_success() throws Exception {
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        mockMvc.perform(delete("/api/prescriptions/" + savedPrescription.getId()))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(0, prescriptionRepository.findByPet(pet).size());
    }

    @Test
    public void testDeletePrescription_notFound() throws Exception {
        mockMvc.perform(delete("/api/prescriptions/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}