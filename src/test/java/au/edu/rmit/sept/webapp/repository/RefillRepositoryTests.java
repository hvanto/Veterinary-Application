package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.model.Refill;
import au.edu.rmit.sept.webapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RefillRepositoryTests {

    @Autowired
    private RefillRepository refillRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    private Pet testPet;
    private Prescription testPrescription;

    @BeforeEach
    public void setUp() {
        // Clean the repository before each test
        refillRepository.deleteAll();
        prescriptionRepository.deleteAll();
        petRepository.deleteAll();
        userRepository.deleteAll();

        // Create a user
        User user = new User();
        user.setPassword("password123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        // Create a pet
        testPet = new Pet();
        testPet.setName("Buddy");
        testPet.setSpecies("Dog");
        testPet.setBreed("Golden Retriever");
        testPet.setGender("Male");
        testPet.setMicrochipped(true);
        testPet.setUser(user);
        petRepository.save(testPet);

        // Create a prescription
        testPrescription = new Prescription(testPet, "Amoxicillin", "Dr. John", "500mg", new Date(), new Date(), "For infection", 12.5);
        prescriptionRepository.save(testPrescription);
    }

    @Test
    public void testAddRefill() {
        // Create a new refill
        Refill refill = new Refill(testPrescription, "John", "Doe", "1234567890", "123 Street",
                "4111111111111111", testPrescription.getCost(), testPrescription.getPet().getUser().getId(),
                "12/25", 123L, new Date(), "TRACKING123");

        // Save the refill
        Refill savedRefill = refillRepository.save(refill);

        // Verify the refill was added
        Optional<Refill> retrievedRefill = refillRepository.findById(savedRefill.getId());
        assertTrue(retrievedRefill.isPresent());
        assertEquals("John", retrievedRefill.get().getFirstName());
    }

    @Test
    public void testFindByUserId() {
        // Create and save a refill
        Refill refill = new Refill(testPrescription, "John", "Doe", "1234567890", "123 Street",
                "4111111111111111", testPrescription.getCost(), testPrescription.getPet().getUser().getId(),
                "12/25", 123L, new Date(), "TRACKING123");
        refillRepository.save(refill);

        // Find refill by user ID
        List<Refill> refills = refillRepository.findByUserId(testPrescription.getPet().getUser().getId());
        assertEquals(1, refills.size());
        assertEquals("John", refills.get(0).getFirstName());
    }

    @Test
    public void testUpdateRefill() {
        // Create and save a refill
        Refill refill = new Refill(testPrescription, "John", "Doe", "1234567890", "123 Street",
                "4111111111111111", testPrescription.getCost(), testPrescription.getPet().getUser().getId(),
                "12/25", 123L, new Date(), "TRACKING123");
        refillRepository.save(refill);

        // Update the refill
        refill.setLastName("Smith");
        refillRepository.save(refill);

        // Verify the update
        Refill updatedRefill = refillRepository.findById(refill.getId()).orElse(null);
        assertNotNull(updatedRefill);
        assertEquals("Smith", updatedRefill.getLastName());
    }

    @Test
    public void testDeleteRefill() {
        // Create and save a refill
        Refill refill = new Refill(testPrescription, "John", "Doe", "1234567890", "123 Street",
                "4111111111111111", testPrescription.getCost(), testPrescription.getPet().getUser().getId(),
                "12/25", 123L, new Date(), "TRACKING123");
        refillRepository.save(refill);

        // Delete the refill
        refillRepository.delete(refill);

        // Verify the refill was deleted
        Optional<Refill> deletedRefill = refillRepository.findById(refill.getId());
        assertFalse(deletedRefill.isPresent());
    }

    @Test
    public void testFindAllRefills() {
        // Create and save two refills
        Refill refill1 = new Refill(testPrescription, "John", "Doe", "1234567890", "123 Street",
                "4111111111111111", testPrescription.getCost(), testPrescription.getPet().getUser().getId(),
                "12/25", 123L, new Date(), "TRACKING123");
        refillRepository.save(refill1);

        Refill refill2 = new Refill(testPrescription, "Jane", "Doe", "0987654321", "456 Avenue",
                "5111111111111111", testPrescription.getCost(), testPrescription.getPet().getUser().getId(),
                "11/24", 456L, new Date(), "TRACKING456");
        refillRepository.save(refill2);

        // Verify that all refills are retrieved
        List<Refill> refills = refillRepository.findAll();
        assertEquals(2, refills.size());
    }

    // Bad Cases

    @Test
    public void testFindNonExistentRefill() {
        // Attempt to find a refill that does not exist
        Optional<Refill> nonExistentRefill = refillRepository.findById(999L); // Assuming 999 is an ID that does not exist
        assertFalse(nonExistentRefill.isPresent());
    }

    @Test
    public void testDeleteNonExistentRefill() {
        // Attempt to delete a refill that does not exist
        Refill refill = new Refill();
        refill.setId(999L); // Set an ID that does not exist

        // Try deleting and check that it does not throw any exception
        assertDoesNotThrow(() -> refillRepository.delete(refill));
    }

    @Test
    public void testAddRefillWithNullFields() {
        // Attempt to add a refill with null fields (assuming prescription is required)
        Refill refill = new Refill();
        refill.setPrescription(null); // Setting prescription to null

        // Try saving and verify it throws a DataIntegrityViolationException
        assertThrows(DataIntegrityViolationException.class, () -> refillRepository.save(refill));
    }
}
