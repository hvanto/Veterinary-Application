package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.PhysicalExam;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the PhysicalExamRepository.
 * This class verifies the custom query methods in the PhysicalExamRepository
 * and ensures the correct behavior when retrieving physical exam records.
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PhysicalExamRepositoryTests {

    @Autowired
    private PhysicalExamRepository physicalExamRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    private Pet testPet;
    private Veterinarian testVeterinarian;

    /**
     * Sets up test data for each test.
     * This method creates and persists a test pet, a test veterinarian,
     * and several physical exam records associated with the pet.
     */
    @BeforeEach
    public void setUp() {
        // Generate a unique identifier to avoid conflicts with existing data
        String uniqueIdentifier = String.valueOf(System.currentTimeMillis());

        // Create and save a test pet using the repository
        testPet = new Pet();
        testPet.setName("Buddy");
        testPet.setSpecies("Dog");
        testPet.setBreed("Golden Retriever");
        petRepository.save(testPet);  // Save via repository

        // Create and save a test veterinarian with a unique email
        testVeterinarian = new Veterinarian();
        testVeterinarian.setFirstName("John");
        testVeterinarian.setLastName("Doe");
        testVeterinarian.setEmail("johndoe" + uniqueIdentifier + "@example.com");  // Unique email
        testVeterinarian.setContact("123456789");
        testVeterinarian.setPassword("password");
        veterinarianRepository.save(testVeterinarian);  // Save via repository

        // Create and save physical exam records directly using the repository
        PhysicalExam exam1 = new PhysicalExam(testPet, LocalDate.of(2023, 1, 1), "John Doe", "Healthy");
        PhysicalExam exam2 = new PhysicalExam(testPet, LocalDate.of(2023, 2, 1), "John Doe", "Minor issues");
        PhysicalExam exam3 = new PhysicalExam(testPet, LocalDate.of(2023, 3, 1), "John Doe", "Requires follow-up");

        physicalExamRepository.save(exam1);
        physicalExamRepository.save(exam2);
        physicalExamRepository.save(exam3);
    }

    /**
     * Tests if the correct physical exam records are retrieved for a specific pet by its ID.
     * It asserts the size of the returned list and verifies the details of the physical exams.
     */
    @Test
    public void testFindByPetId() {
        // Fetch physical exam records for the test pet by its ID
        List<PhysicalExam> physicalExamList = physicalExamRepository.findByPetId(testPet.getId());

        // Assert the size of the returned list matches the number of records created
        assertThat(physicalExamList).hasSize(3);

        // Convert java.util.Date to LocalDate for comparison
        LocalDate examDate1 = convertToLocalDate(physicalExamList.get(0).getExamDate());
        LocalDate examDate2 = convertToLocalDate(physicalExamList.get(1).getExamDate());
        LocalDate examDate3 = convertToLocalDate(physicalExamList.get(2).getExamDate());

        // Assert that the records' dates and notes are correct
        assertThat(examDate1).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(physicalExamList.get(0).getNotes()).isEqualTo("Healthy");

        assertThat(examDate2).isEqualTo(LocalDate.of(2023, 2, 1));
        assertThat(physicalExamList.get(1).getNotes()).isEqualTo("Minor issues");

        assertThat(examDate3).isEqualTo(LocalDate.of(2023, 3, 1));
        assertThat(physicalExamList.get(2).getNotes()).isEqualTo("Requires follow-up");
    }

    /**
     * Helper method to convert java.util.Date to java.time.LocalDate.
     * Handles java.sql.Date specifically using its toLocalDate() method,
     * and for other types of Date, it uses toInstant() conversion.
     *
     * @param date the Date to be converted
     * @return the LocalDate equivalent of the given Date
     */
    private LocalDate convertToLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            // Handle java.sql.Date using toLocalDate()
            return ((java.sql.Date) date).toLocalDate();
        } else {
            // For other types of Date, use toInstant() conversion
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

    /**
     * Tests fetching physical exam records for a pet that doesn't exist.
     * Expects an empty list to be returned.
     */
    @Test
    public void testFindByPetId_NoRecords() {
        // Fetch physical exams for a non-existent pet (ID = 999L)
        List<PhysicalExam> physicalExamList = physicalExamRepository.findByPetId(999L);

        // Assert that the returned list is empty
        assertThat(physicalExamList).isEmpty();
    }
}
