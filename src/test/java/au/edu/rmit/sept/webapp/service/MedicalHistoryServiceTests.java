package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.MedicalHistory;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.repository.MedicalHistoryRepository;
import au.edu.rmit.sept.webapp.repository.PetRepository;
import au.edu.rmit.sept.webapp.repository.VeterinarianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test class for {@link MedicalHistoryService}.
 * This class tests the service methods in conjunction with the actual database.
 * It verifies the functionality for fetching and saving medical history records.
 */
@SpringBootTest
public class MedicalHistoryServiceTests {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    private Pet testPet;
    private Veterinarian testVeterinarian;
    private MedicalHistory medicalHistory1;
    private MedicalHistory medicalHistory2;

    /**
     * Sets up the test environment by creating a test pet, veterinarian, and medical history records.
     * This method is executed before each test.
     */
    @BeforeEach
    @Transactional
    public void setUp() {
        // Create and save a test pet
        testPet = new Pet();
        testPet.setName("Buddy");
        testPet = petRepository.save(testPet); // Save and assign ID

        // Create and save a test veterinarian
        testVeterinarian = new Veterinarian();
        testVeterinarian.setFirstName("Doe");
        testVeterinarian = veterinarianRepository.save(testVeterinarian);  // Save veterinarian

        // Create and save test medical history records for the pet
        medicalHistory1 = new MedicalHistory(testPet, "Dr. John", "Checkup", testVeterinarian, new java.util.Date(), "All good", null);
        medicalHistory2 = new MedicalHistory(testPet, "Dr. Smith", "Vaccination", testVeterinarian, new java.util.Date(), "Rabies vaccine", null);

        medicalHistoryRepository.save(medicalHistory1);
        medicalHistoryRepository.save(medicalHistory2);
    }

    /**
     * Tests the method {@link MedicalHistoryService#getMedicalHistoryByPetId(Long)}.
     * This test verifies that the correct medical history records are fetched for a pet by its ID.
     */
    @Test
    @Transactional
    public void testGetMedicalHistoryByPetId() {
        // Call the service method to fetch medical history by pet ID
        List<MedicalHistory> medicalHistoryList = medicalHistoryService.getMedicalHistoryByPetId(testPet.getId());

        // Verify the result
        assertNotNull(medicalHistoryList);
        assertEquals(2, medicalHistoryList.size());
        assertEquals("Checkup", medicalHistoryList.get(0).getTreatment());
        assertEquals("Vaccination", medicalHistoryList.get(1).getTreatment());
    }

    /**
     * Tests the method {@link MedicalHistoryService#getMedicalHistoryByPetId(Long)} when there are no records.
     * This test verifies that an empty list is returned when no medical history records exist for a pet.
     */
    @Test
    @Transactional
    public void testGetMedicalHistoryByPetId_Empty() {
        // Create a new pet with no medical history
        Pet newPet = new Pet();
        newPet.setName("Max");
        newPet = petRepository.save(newPet);

        // Fetch medical history for the new pet, which should be empty
        List<MedicalHistory> medicalHistoryList = medicalHistoryService.getMedicalHistoryByPetId(newPet.getId());

        // Verify the result
        assertNotNull(medicalHistoryList);
        assertTrue(medicalHistoryList.isEmpty());
    }

    /**
     * Tests the method {@link MedicalHistoryService#save(MedicalHistory)} for saving a new medical history record.
     * This test verifies that a new medical history record can be correctly saved and retrieved from the database.
     */
    @Test
    @Transactional
    public void testSaveMedicalHistory_NewRecord() {
        // Create a new medical history record
        MedicalHistory newMedicalHistory = new MedicalHistory(testPet, "Dr. Brown", "Surgery", testVeterinarian, new java.util.Date(), "Appendectomy", null);

        // Save the new medical history record using the service
        medicalHistoryService.save(newMedicalHistory);

        // Fetch it from the database
        MedicalHistory savedRecord = medicalHistoryRepository.findById(newMedicalHistory.getId()).orElse(null);

        // Verify that it was saved correctly
        assertNotNull(savedRecord);
        assertEquals("Surgery", savedRecord.getTreatment());
        assertEquals("Dr. Brown", savedRecord.getPractitioner());
    }

    /**
     * Tests the method {@link MedicalHistoryService#save(MedicalHistory)} for updating an existing record.
     * This test verifies that an existing medical history record can be correctly updated in the database.
     */
    @Test
    @Transactional
    public void testSaveMedicalHistory_ExistingRecord() {
        // Update the first medical history record's treatment
        medicalHistory1.setTreatment("Updated Checkup");

        // Save the updated record using the service
        medicalHistoryService.save(medicalHistory1);

        // Fetch the updated record from the database
        MedicalHistory updatedRecord = medicalHistoryRepository.findById(medicalHistory1.getId()).orElse(null);

        // Verify that the update was saved correctly
        assertNotNull(updatedRecord);
        assertEquals("Updated Checkup", updatedRecord.getTreatment());
    }
}