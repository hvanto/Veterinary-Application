package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.model.WeightRecord;
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
 * Unit tests for WeightRecordRepository.
 * This class tests the custom query method in the WeightRecordRepository
 * and ensures the correct behavior for retrieving weight records.
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WeightRecordRepositoryTests {

    @Autowired
    private WeightRecordRepository weightRecordRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    private Pet testPet;
    private Veterinarian testVeterinarian;

    @BeforeEach
    public void setUp() {
        // Create and save a test pet directly using the repository
        testPet = new Pet();
        testPet.setName("Buddy");
        testPet.setSpecies("Dog");
        testPet.setBreed("Golden Retriever");
        petRepository.save(testPet);

        // Create and save a test veterinarian directly using the repository
        String uniqueEmail = "johndoe" + System.currentTimeMillis() + "@example.com";  // Ensure unique email
        testVeterinarian = new Veterinarian();
        testVeterinarian.setFirstName("John");
        testVeterinarian.setLastName("Doe");
        testVeterinarian.setEmail(uniqueEmail);
        testVeterinarian.setContact("123456789");
        testVeterinarian.setPassword("password");
        veterinarianRepository.save(testVeterinarian);

        // Create and save weight records directly using the repository
        WeightRecord weightRecord1 = new WeightRecord(testPet, convertToDate(LocalDate.of(2023, 1, 1)), 12.5);
        WeightRecord weightRecord2 = new WeightRecord(testPet, convertToDate(LocalDate.of(2023, 2, 1)), 13.0);
        WeightRecord weightRecord3 = new WeightRecord(testPet, convertToDate(LocalDate.of(2023, 3, 1)), 13.5);

        weightRecordRepository.save(weightRecord1);
        weightRecordRepository.save(weightRecord2);
        weightRecordRepository.save(weightRecord3);
    }

    @Test
    public void testFindByPetId() {
        // Fetch weight records for the test pet
        List<WeightRecord> weightRecordList = weightRecordRepository.findByPetId(testPet.getId());

        // Assert the size of the returned list is correct
        assertThat(weightRecordList).hasSize(3);

        // Assert the records are correct
        assertThat(weightRecordList.get(0).getWeight()).isEqualTo(12.5);
        assertThat(convertToLocalDate(weightRecordList.get(0).getRecordDate())).isEqualTo(LocalDate.of(2023, 1, 1));

        assertThat(weightRecordList.get(1).getWeight()).isEqualTo(13.0);
        assertThat(convertToLocalDate(weightRecordList.get(1).getRecordDate())).isEqualTo(LocalDate.of(2023, 2, 1));

        assertThat(weightRecordList.get(2).getWeight()).isEqualTo(13.5);
        assertThat(convertToLocalDate(weightRecordList.get(2).getRecordDate())).isEqualTo(LocalDate.of(2023, 3, 1));
    }

    @Test
    public void testFindByPetId_NoRecords() {
        // Fetch weight records for a non-existent pet (should return an empty list)
        List<WeightRecord> weightRecordList = weightRecordRepository.findByPetId(999L);

        // Assert that the list is empty
        assertThat(weightRecordList).isEmpty();
    }

    // Utility method to convert LocalDate to java.util.Date
    private Date convertToDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);  // Use java.sql.Date for persistence
    }

    // Utility method to convert java.util.Date to LocalDate
    private LocalDate convertToLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            // Handle java.sql.Date specifically using toLocalDate()
            return ((java.sql.Date) date).toLocalDate();
        } else {
            // For other types of Date, use the toInstant conversion
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }
}
