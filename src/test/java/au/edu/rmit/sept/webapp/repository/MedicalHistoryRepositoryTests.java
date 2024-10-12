package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.MedicalHistory;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for MedicalHistoryRepository.
 * This class tests the custom query method in the MedicalHistoryRepository
 * and ensures the correct behavior for retrieving medical history records.
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MedicalHistoryRepositoryTests {

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    private Pet testPet;
    private Veterinarian testVeterinarian;

    @BeforeEach
    public void setUp() {
        // Generate a unique identifier (e.g., current timestamp) to ensure unique email addresses
        String uniqueIdentifier = String.valueOf(System.currentTimeMillis());

        // Create and save a test pet directly using the repository
        testPet = new Pet();
        testPet.setName("Buddy");
        testPet.setSpecies("Dog");
        testPet.setBreed("Golden Retriever");
        petRepository.save(testPet);  // Persist directly via repository

        // Create and save a test veterinarian directly using the repository
        testVeterinarian = new Veterinarian();
        testVeterinarian.setFirstName("John");
        testVeterinarian.setLastName("Doe");
        testVeterinarian.setEmail("johndoe" + uniqueIdentifier + "@example.com"); // Unique email
        testVeterinarian.setContact("123456789");
        testVeterinarian.setPassword("password");
        veterinarianRepository.save(testVeterinarian);  // Persist directly via repository

        // Create and save medical history records directly using the repository
        MedicalHistory history1 = new MedicalHistory(testPet, "John Doe", "Fever", testVeterinarian, Date.valueOf(LocalDate.of(2023, 1, 1)), "Prescribed medication", null);
        MedicalHistory history2 = new MedicalHistory(testPet, "John Doe", "Infection", testVeterinarian, Date.valueOf(LocalDate.of(2023, 2, 1)), "Prescribed antibiotics", null);
        MedicalHistory history3 = new MedicalHistory(testPet, "John Doe", "Injury", testVeterinarian, Date.valueOf(LocalDate.of(2023, 3, 1)), "Wound treatment", null);

        medicalHistoryRepository.save(history1);
        medicalHistoryRepository.save(history2);
        medicalHistoryRepository.save(history3);
    }

    @Test
    public void testFindByPetIdOrderByEventDateDesc() {
        // Fetch medical history records for the test pet, ordered by event date descending
        List<MedicalHistory> medicalHistoryList = medicalHistoryRepository.findByPetIdOrderByEventDateDesc(testPet.getId());

        // Assert the size of the returned list is correct
        assertThat(medicalHistoryList).hasSize(3);

        // Assert the records are in the correct order (most recent first)
        assertThat(medicalHistoryList.get(0).getEventDate()).isEqualTo(Date.valueOf(LocalDate.of(2023, 3, 1)));
        assertThat(medicalHistoryList.get(1).getEventDate()).isEqualTo(Date.valueOf(LocalDate.of(2023, 2, 1)));
        assertThat(medicalHistoryList.get(2).getEventDate()).isEqualTo(Date.valueOf(LocalDate.of(2023, 1, 1)));
    }

    @Test
    public void testFindByPetIdOrderByEventDateDesc_NoRecords() {
        // Fetch medical history for a pet that doesn't exist (should return an empty list)
        List<MedicalHistory> medicalHistoryList = medicalHistoryRepository.findByPetIdOrderByEventDateDesc(999L);

        // Assert that the list is empty
        assertThat(medicalHistoryList).isEmpty();
    }

    @Test
    public void testFindByPetIdOrderByEventDateDesc_SameDateRecords() {
        // Create two records with the same event date
        MedicalHistory history4 = new MedicalHistory(testPet, "John Doe", "Cold", testVeterinarian, Date.valueOf(LocalDate.of(2023, 3, 1)), "Prescribed cold medicine", null);
        MedicalHistory history5 = new MedicalHistory(testPet, "John Doe", "Allergy", testVeterinarian, Date.valueOf(LocalDate.of(2023, 3, 1)), "Prescribed antihistamine", null);
        medicalHistoryRepository.save(history4);
        medicalHistoryRepository.save(history5);

        // Fetch medical history records for the test pet
        List<MedicalHistory> medicalHistoryList = medicalHistoryRepository.findByPetIdOrderByEventDateDesc(testPet.getId());

        // Assert the size is correct (5 records in total)
        assertThat(medicalHistoryList).hasSize(5);

        // Assert the records with the same event date are returned in the correct order
        assertThat(medicalHistoryList.get(0).getTreatment()).isEqualTo("Injury");
        assertThat(medicalHistoryList.get(1).getTreatment()).isEqualTo("Cold");   // "Cold" comes first in this case
        assertThat(medicalHistoryList.get(2).getTreatment()).isEqualTo("Allergy"); // "Allergy" comes second
        assertThat(medicalHistoryList.get(3).getEventDate()).isEqualTo(Date.valueOf(LocalDate.of(2023, 2, 1)));
    }

    @Test
    public void testFindByPetIdOrderByEventDateDesc_NullPetId() {
        // Fetch medical history records with a null pet ID
        List<MedicalHistory> medicalHistoryList = medicalHistoryRepository.findByPetIdOrderByEventDateDesc(null);

        // Assert that the list is empty (no records should be returned)
        assertThat(medicalHistoryList).isEmpty();
    }
}