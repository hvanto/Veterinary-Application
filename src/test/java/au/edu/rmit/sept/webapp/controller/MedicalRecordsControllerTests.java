package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.*;
import au.edu.rmit.sept.webapp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Date;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * This class contains unit tests for the MedicalRecordsController in a Spring Boot application.
 * It uses MockMvc to simulate HTTP requests and verify the behavior of the controller methods.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MedicalRecordsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VaccinationRepository vaccinationRepository;

    @Autowired
    private WeightRecordRepository weightRecordRepository;

    @Autowired
    private TreatmentPlanRepository treatmentPlanRepository;

    @Autowired
    private PhysicalExamRepository physicalExamRepository;

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Pet testPet;
    private Veterinarian testVeterinarian;
    private Appointment testAppointment;

    /**
     * This method sets up test data before each test. It creates a test user, pet, veterinarian,
     * appointment, and seeds the database with additional related data such as weight records and
     * vaccinations.
     */
    @BeforeEach
    public void setUp() {
        // Create unique test data using dynamic identifiers (current timestamp)
        String uniqueIdentifier = String.valueOf(System.currentTimeMillis());

        // Create a test user with unique email
        testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("testuser" + uniqueIdentifier + "@example.com");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        // Create a test pet with a unique name
        testPet = new Pet();
        testPet.setName("Buddy" + uniqueIdentifier);
        testPet.setSpecies("Dog");
        testPet.setBreed("Golden Retriever");
        testPet.setUser(testUser);
        petRepository.save(testPet);

        // Create a test veterinarian with unique email
        testVeterinarian = new Veterinarian();
        testVeterinarian.setFirstName("John");
        testVeterinarian.setLastName("Doe");
        testVeterinarian.setEmail("johndoe" + uniqueIdentifier + "@example.com");
        testVeterinarian.setContact("123456789");
        testVeterinarian.setPassword("password");
        veterinarianRepository.save(testVeterinarian);

        // Create a test appointment
        testAppointment = new Appointment();
        testAppointment.setPet(testPet);
        testAppointment.setAppointmentDate(Date.valueOf(LocalDate.now()));
        testAppointment.setVeterinarian(testVeterinarian);
        testAppointment.setUser(testUser);
        appointmentRepository.save(testAppointment);

        // Seed additional data (weight records, vaccinations, etc.)
        WeightRecord weightRecord = new WeightRecord(testPet, java.sql.Date.valueOf(LocalDate.of(2023, 1, 1)), 5.5);
        weightRecordRepository.save(weightRecord);

        Vaccination vaccination = new Vaccination(testPet, "Rabies", java.sql.Date.valueOf(LocalDate.of(2023, 1, 1)), "John Doe", java.sql.Date.valueOf(LocalDate.of(2024, 1, 1)));
        vaccinationRepository.save(vaccination);

        PhysicalExam physicalExam = new PhysicalExam(testPet, LocalDate.of(2023, 1, 1), "John Doe", "All clear");
        physicalExamRepository.save(physicalExam);

        MedicalHistory medicalHistory = new MedicalHistory(testPet, "John Doe", "Fever", testVeterinarian, java.sql.Date.valueOf(LocalDate.of(2023, 1, 1)), "Prescribed antipyretic", null);
        medicalHistoryRepository.save(medicalHistory);

        TreatmentPlan treatmentPlan = new TreatmentPlan(testPet, LocalDate.of(2023, 1, 1), "Treatment for arthritis", "John Doe", "None");
        treatmentPlanRepository.save(treatmentPlan);
    }

    /**
     * Test fetching all pets for a specific user. Expects a successful HTTP 200 status.
     */
    @Test
    public void getUserPets_Success() throws Exception {
        mockMvc.perform(get("/api/medical-records/user-pets")
                        .param("userId", "1") // Replace with valid user ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty());
    }

    /**
     * Test that pets are seeded when no pets exist for a user.
     * Expects that the response contains pets after seeding.
     */
    @Test
    public void getUserPets_NoPets_SeedsData() throws Exception {
        mockMvc.perform(get("/api/medical-records/user-pets")
                        .param("userId", "2") // Replace with a user ID that has no pets
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty()); // Expect pets after seeding
    }

    /**
     * Test fetching detailed medical records for a selected pet.
     * Expects a successful HTTP 200 status and various medical records returned.
     */
    @Test
    public void getMedicalRecords_Success() throws Exception {
        mockMvc.perform(get("/api/medical-records/" + testPet.getId()) // Replace with valid pet ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.selectedPet").exists()) // Verify pet details exist
                .andExpect(jsonPath("$.medicalHistoryList.length()").isNotEmpty())
                .andExpect(jsonPath("$.physicalExamList.length()").isNotEmpty())
                .andExpect(jsonPath("$.vaccinationList.length()").isNotEmpty())
                .andExpect(jsonPath("$.weightRecords.length()").isNotEmpty())
                .andExpect(jsonPath("$.treatmentPlanList.length()").isNotEmpty());
    }

    /**
     * Test downloading medical records as a PDF file.
     * Expects a successful HTTP 200 status and a PDF response.
     */
    @Test
    public void downloadMedicalRecords_PDF_Success() throws Exception {
        mockMvc.perform(get("/api/medical-records/download")
                        .param("selectedPetId", String.valueOf(testPet.getId())) // Replace with valid pet ID
                        .param("format", "pdf")
                        .param("sections", "medicalHistory,physicalExam,vaccinations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/pdf"));
    }

    /**
     * Test downloading medical records as an XML file.
     * Expects a successful HTTP 200 status and an XML response.
     */
    @Test
    public void downloadMedicalRecords_XML_Success() throws Exception {
        mockMvc.perform(get("/api/medical-records/download")
                        .param("selectedPetId", String.valueOf(testPet.getId())) // Replace with valid pet ID
                        .param("format", "xml")
                        .param("sections", "medicalHistory,physicalExam,vaccinations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/xml"));
    }

    /**
     * Test uploading a weight record for a pet's appointment.
     * Expects a successful HTTP 200 status and confirmation message.
     */
    @Test
    public void uploadWeightRecord_Success() throws Exception {
        mockMvc.perform(post("/api/medical-records/upload-records")
                        .param("appointmentId", String.valueOf(testAppointment.getId())) // Replace with valid appointment ID
                        .param("category", "weight-record")
                        .param("veterinarianId", String.valueOf(testVeterinarian.getId())) // Replace with valid veterinarian ID
                        .param("weight", "6.5")
                        .param("date", "2023-10-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Weight record uploaded successfully."));
    }

    /**
     * Test uploading a vaccination record for a pet's appointment.
     * Expects a successful HTTP 200 status and confirmation message.
     */
    @Test
    public void uploadVaccinationRecord_Success() throws Exception {
        mockMvc.perform(post("/api/medical-records/upload-records")
                        .param("appointmentId", String.valueOf(testAppointment.getId())) // Replace with valid appointment ID
                        .param("category", "vaccination")
                        .param("veterinarianId", String.valueOf(testVeterinarian.getId())) // Replace with valid veterinarian ID
                        .param("vaccineName", "Rabies")
                        .param("vaccinationDate", "2023-10-01")
                        .param("administeredBy", "John Doe")
                        .param("nextDueDate", "2024-10-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Vaccination record uploaded successfully."));
    }

    /**
     * Test uploading a treatment plan for a pet's appointment.
     * Expects a successful HTTP 200 status and confirmation message.
     */
    @Test
    public void uploadTreatmentPlan_Success() throws Exception {
        mockMvc.perform(post("/api/medical-records/upload-records")
                        .param("appointmentId", String.valueOf(testAppointment.getId())) // Replace with valid appointment ID
                        .param("category", "treatment-plan")
                        .param("veterinarianId", String.valueOf(testVeterinarian.getId())) // Replace with valid veterinarian ID
                        .param("planDate", "2023-10-01")
                        .param("description", "Arthritis Management")
                        .param("practitioner", "Dr. Smith")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Treatment plan uploaded successfully."));
    }

    /**
     * Test uploading a medical history record for a pet's appointment.
     * Expects a successful HTTP 200 status and confirmation message.
     */
    @Test
    public void uploadMedicalHistory_Success() throws Exception {
        mockMvc.perform(post("/api/medical-records/upload-records")
                        .param("appointmentId", String.valueOf(testAppointment.getId())) // Replace with valid appointment ID
                        .param("category", "medical-history")
                        .param("veterinarianId", String.valueOf(testVeterinarian.getId())) // Replace with valid veterinarian ID
                        .param("eventDate", "2023-10-01")
                        .param("treatment", "Fever treatment")
                        .param("practitioner", "Dr. Doe")
                        .param("notes", "Administered fever medication")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Medical history uploaded successfully."));
    }

    /**
     * Test uploading a physical exam record for a pet's appointment.
     * Expects a successful HTTP 200 status and confirmation message.
     */
    @Test
    public void uploadPhysicalExam_Success() throws Exception {
        mockMvc.perform(post("/api/medical-records/upload-records")
                        .param("appointmentId", String.valueOf(testAppointment.getId())) // Replace with valid appointment ID
                        .param("category", "physical-exam")
                        .param("veterinarianId", String.valueOf(testVeterinarian.getId())) // Replace with valid veterinarian ID
                        .param("examDate", "2023-10-01")
                        .param("notes", "Routine checkup, all clear")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Physical exam uploaded successfully."));
    }
}
