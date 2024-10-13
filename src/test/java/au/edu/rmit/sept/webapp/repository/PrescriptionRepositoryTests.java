package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Prescription;
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
public class PrescriptionRepositoryTests {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    private Pet testPet;

    @BeforeEach
    public void setUp() {
        // Clean the repository before each test
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

        // Create initial prescriptions
        Prescription prescription1 = new Prescription(testPet, "Amoxicillin", "Dr. John", "500mg", new Date(), new Date(), "For infection", 12.5);
        Prescription prescription2 = new Prescription(testPet, "Ibuprofen", "Dr. Jane", "200mg", new Date(), new Date(), "For pain relief", 8.0);

        prescriptionRepository.save(prescription1);
        prescriptionRepository.save(prescription2);
    }

    @Test
    public void testFindByPet() {
        // Test custom query: findByPet
        List<Prescription> prescriptions = prescriptionRepository.findByPet(testPet);
        assertEquals(2, prescriptions.size());
        assertEquals("Amoxicillin", prescriptions.get(0).getPrescription());
        assertEquals("Ibuprofen", prescriptions.get(1).getPrescription());
    }

    @Test
    public void testAddPrescription() {
        // Add a new prescription
        Prescription newPrescription = new Prescription(testPet, "Paracetamol", "Dr. Smith", "500mg", new Date(), new Date(), "For fever", 5.5);
        Prescription savedPrescription = prescriptionRepository.save(newPrescription);

        // Verify the prescription was added
        Optional<Prescription> retrievedPrescription = prescriptionRepository.findById(savedPrescription.getId());
        assertTrue(retrievedPrescription.isPresent());
        assertEquals("Paracetamol", retrievedPrescription.get().getPrescription());
    }

    @Test
    public void testUpdatePrescription() {
        // Get one of the existing prescriptions
        List<Prescription> prescriptions = prescriptionRepository.findByPet(testPet);
        Prescription prescription = prescriptions.get(0);

        // Update the prescription
        prescription.setDosage("750mg");
        prescriptionRepository.save(prescription);

        // Verify the update
        Prescription updatedPrescription = prescriptionRepository.findById(prescription.getId()).orElse(null);
        assertNotNull(updatedPrescription);
        assertEquals("750mg", updatedPrescription.getDosage());
    }

    @Test
    public void testDeletePrescription() {
        // Get one of the existing prescriptions
        List<Prescription> prescriptions = prescriptionRepository.findByPet(testPet);
        Prescription prescriptionToDelete = prescriptions.get(0);

        // Delete the prescription
        prescriptionRepository.delete(prescriptionToDelete);

        // Verify the prescription was deleted
        Optional<Prescription> deletedPrescription = prescriptionRepository.findById(prescriptionToDelete.getId());
        assertFalse(deletedPrescription.isPresent());
    }

    @Test
    public void testFindAllPrescriptions() {
        // Verify that all prescriptions are retrieved
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        assertEquals(2, prescriptions.size());
    }

    @Test
    public void testAddPrescriptionWithNullFields() {
        // Attempt to add a prescription with null required fields
        Prescription prescription = new Prescription();
        prescription.setPet(testPet);
        prescription.setPrescription(null); // Required field
        prescription.setPractitioner("Dr. Smith");
        prescription.setDosage("500mg");
        prescription.setStartDate(new Date());
        prescription.setEndDate(new Date());
        prescription.setDescription("Test description");
        prescription.setCost(10.0);

        // Expect DataIntegrityViolationException when saving
        assertThrows(DataIntegrityViolationException.class, () -> prescriptionRepository.save(prescription));
    }

    @Test
    public void testUpdatePrescriptionWithNullFields() {
        // Get an existing prescription
        List<Prescription> prescriptions = prescriptionRepository.findByPet(testPet);
        Prescription prescription = prescriptions.get(0);

        // Update to null required field
        prescription.setPrescription(null); // Required field

        // Expect DataIntegrityViolationException when saving
        assertThrows(DataIntegrityViolationException.class, () -> prescriptionRepository.save(prescription));
    }

    @Test
    public void testAddPrescriptionWithInvalidDosage() {
        // Add a new prescription with invalid dosage
        Prescription invalidDosagePrescription = new Prescription(testPet, "Paracetamol", "Dr. Smith", null, new Date(), new Date(), "For fever", 5.5);

        // Assuming an empty dosage is invalid
        assertThrows(DataIntegrityViolationException.class, () -> prescriptionRepository.save(invalidDosagePrescription));
    }

    @Test
    public void testDeleteNonExistentPrescription() {
        // Create a prescription object with a non-existent ID
        Long nonExistentId = 999L; // An ID that doesn't exist

        // Attempt to delete a prescription that doesn't exist
        // Expecting nothing to happen, but we can verify it by trying to find it
        Optional<Prescription> deletedPrescription = prescriptionRepository.findById(nonExistentId);

        assertFalse(deletedPrescription.isPresent(), "Prescription should not exist before deletion attempt");

        // Now, we can try to delete it and verify it still doesn't throw an exception
        assertDoesNotThrow(() -> prescriptionRepository.deleteById(nonExistentId));

        // Verify that it still does not exist
        Optional<Prescription> afterDeletion = prescriptionRepository.findById(nonExistentId);
        assertFalse(afterDeletion.isPresent(), "Prescription should still not exist after deletion attempt");
    }

}

