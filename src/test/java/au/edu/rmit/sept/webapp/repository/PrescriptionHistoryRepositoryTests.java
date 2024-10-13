package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.MedicalHistory;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.PrescriptionHistory;
import au.edu.rmit.sept.webapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PrescriptionHistoryRepositoryTests {

    @Autowired
    private PrescriptionHistoryRepository prescriptionHistoryRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    private Pet testPet;

    @BeforeEach
    public void setUp() {
        // Delete all prescription histories before each test
        medicalHistoryRepository.deleteAll();
        prescriptionHistoryRepository.deleteAll();
        petRepository.deleteAll();
        userRepository.deleteAll();

        // Add a test User (owner of the pet)
        User testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("johndoe@example.com");
        userRepository.save(testUser);

        // Add a test Pet
        testPet = new Pet(testUser, "Buddy", "Dog", "Golden Retriever", "Male", true, "Healthy", "/images/buddy.png", LocalDate.of(2018, 6, 12));
        petRepository.save(testPet);

        // Add test data to PrescriptionHistory
        PrescriptionHistory history1 = new PrescriptionHistory(
                testPet, "Dr. John Smith", "Painkiller", "Dr. Emily",
                "10mg", new Date(), null, "For joint pain"
        );

        PrescriptionHistory history2 = new PrescriptionHistory(
                testPet, "Dr. Jane Doe", "Antibiotic", null,
                "500mg", new Date(), null, "For infection"
        );

        prescriptionHistoryRepository.save(history1);
        prescriptionHistoryRepository.save(history2);
    }

    @Test
    public void testFindByPet() {
        // Retrieve prescription histories for the testPet
        List<PrescriptionHistory> histories = prescriptionHistoryRepository.findByPet(testPet);

        // Verify the retrieved histories
        assertEquals(2, histories.size());
        assertTrue(histories.stream().anyMatch(h -> h.getPractitioner().equals("Dr. John Smith")));
        assertTrue(histories.stream().anyMatch(h -> h.getPrescription().equals("Antibiotic")));
    }

    @Test
    public void testPrescriptionDetails() {
        // Retrieve all histories for the test pet
        List<PrescriptionHistory> histories = prescriptionHistoryRepository.findByPet(testPet);

        // Check specific details of the first history entry
        PrescriptionHistory history1 = histories.get(0);

        assertEquals("Dr. John Smith", history1.getPractitioner());
        assertEquals("Painkiller", history1.getPrescription());
        assertEquals("For joint pain", history1.getMoreInformation());
    }

    @Test
    public void testFindByPet_NoHistory() {
        // Create a new pet with no prescription history
        User newUser = new User();
        newUser.setFirstName("Alice");
        newUser.setLastName("Wonder");
        newUser.setEmail("alice@example.com");
        userRepository.save(newUser);

        Pet newPet = new Pet(newUser, "Charlie", "Cat", "Siamese", "Female", false, "Playful", "/images/charlie.png", LocalDate.of(2020, 2, 25));
        petRepository.save(newPet);

        // Attempt to retrieve histories for the new pet
        List<PrescriptionHistory> histories = prescriptionHistoryRepository.findByPet(newPet);

        // Verify that no histories are found
        assertEquals(0, histories.size());
    }
}
