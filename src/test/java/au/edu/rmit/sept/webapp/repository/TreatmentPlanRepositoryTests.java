package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.TreatmentPlan;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for TreatmentPlanRepository.
 * This class tests the custom query method in the TreatmentPlanRepository
 * and ensures the correct behavior for retrieving treatment plan records.
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TreatmentPlanRepositoryTests {

    @Autowired
    private TreatmentPlanRepository treatmentPlanRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    private Pet testPet;
    private Veterinarian testVeterinarian;

    /**
     * Sets up test data before each test.
     * This method creates and persists a test pet, a test veterinarian,
     * and several treatment plan records associated with the pet.
     */
    @BeforeEach
    public void setUp() {
        // Generate a unique identifier to ensure unique data for each test
        String uniqueIdentifier = String.valueOf(System.currentTimeMillis());

        // Create and save a test pet directly using the repository
        testPet = new Pet();
        testPet.setName("Buddy");
        testPet.setSpecies("Dog");
        testPet.setBreed("Golden Retriever");
        petRepository.save(testPet);  // Persist via repository

        // Create and save a test veterinarian with unique email
        testVeterinarian = new Veterinarian();
        testVeterinarian.setFirstName("John");
        testVeterinarian.setLastName("Doe");
        testVeterinarian.setEmail("johndoe" + uniqueIdentifier + "@example.com"); // Unique email
        testVeterinarian.setContact("123456789");
        testVeterinarian.setPassword("password");
        veterinarianRepository.save(testVeterinarian);  // Persist via repository

        // Create and save treatment plan records directly using the repository
        TreatmentPlan plan1 = new TreatmentPlan(testPet, LocalDate.of(2023, 1, 1), "Plan for arthritis", "John Doe", "Follow-up in 1 month");
        TreatmentPlan plan2 = new TreatmentPlan(testPet, LocalDate.of(2023, 2, 1), "Plan for dental care", "John Doe", "Routine cleaning");
        TreatmentPlan plan3 = new TreatmentPlan(testPet, LocalDate.of(2023, 3, 1), "Plan for injury recovery", "John Doe", "Rest and medication");

        treatmentPlanRepository.save(plan1);
        treatmentPlanRepository.save(plan2);
        treatmentPlanRepository.save(plan3);
    }

    /**
     * Tests if the correct treatment plan records are retrieved for a specific pet by its ID.
     * It asserts the size of the returned list and verifies the details of the treatment plans.
     */
    @Test
    public void testFindByPetId() {
        // Fetch treatment plan records for the test pet by its ID
        List<TreatmentPlan> treatmentPlanList = treatmentPlanRepository.findByPetId(testPet.getId());

        // Assert the size of the returned list matches the number of records created
        assertThat(treatmentPlanList).hasSize(3);

        // Assert the records' details are correct
        assertThat(treatmentPlanList.get(0).getPlanDate()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(treatmentPlanList.get(0).getDescription()).isEqualTo("Plan for arthritis");
        assertThat(treatmentPlanList.get(0).getPractitioner()).isEqualTo("John Doe");
        assertThat(treatmentPlanList.get(0).getNotes()).isEqualTo("Follow-up in 1 month");

        assertThat(treatmentPlanList.get(1).getPlanDate()).isEqualTo(LocalDate.of(2023, 2, 1));
        assertThat(treatmentPlanList.get(1).getDescription()).isEqualTo("Plan for dental care");
        assertThat(treatmentPlanList.get(1).getPractitioner()).isEqualTo("John Doe");
        assertThat(treatmentPlanList.get(1).getNotes()).isEqualTo("Routine cleaning");

        assertThat(treatmentPlanList.get(2).getPlanDate()).isEqualTo(LocalDate.of(2023, 3, 1));
        assertThat(treatmentPlanList.get(2).getDescription()).isEqualTo("Plan for injury recovery");
        assertThat(treatmentPlanList.get(2).getPractitioner()).isEqualTo("John Doe");
        assertThat(treatmentPlanList.get(2).getNotes()).isEqualTo("Rest and medication");
    }

    /**
     * Tests fetching treatment plan records for a pet that doesn't exist.
     * Expects an empty list to be returned.
     */
    @Test
    public void testFindByPetId_NoRecords() {
        // Fetch treatment plans for a pet that doesn't exist (ID = 999L)
        List<TreatmentPlan> treatmentPlanList = treatmentPlanRepository.findByPetId(999L);

        // Assert that the returned list is empty
        assertThat(treatmentPlanList).isEmpty();
    }
}
