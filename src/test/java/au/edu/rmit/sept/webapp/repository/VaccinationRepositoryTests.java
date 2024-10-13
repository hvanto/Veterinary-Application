package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Vaccination;
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
 * Unit tests for VaccinationRepository.
 * This class tests the custom query method in the VaccinationRepository
 * and ensures the correct behavior for retrieving vaccination records.
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VaccinationRepositoryTests {

    @Autowired
    private VaccinationRepository vaccinationRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    private Pet testPet;
    private Veterinarian testVeterinarian;

    /**
     * Sets up test data before each test.
     * This method creates and persists a test pet, a test veterinarian,
     * and several vaccination records associated with the pet.
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

        // Create and save a test veterinarian with a unique email
        testVeterinarian = new Veterinarian();
        testVeterinarian.setFirstName("John");
        testVeterinarian.setLastName("Doe");
        testVeterinarian.setEmail("johndoe" + uniqueIdentifier + "@example.com"); // Unique email
        testVeterinarian.setContact("123456789");
        testVeterinarian.setPassword("password");
        veterinarianRepository.save(testVeterinarian);  // Persist via repository

        // Create and save vaccination records directly using the repository
        Vaccination vaccination1 = new Vaccination(testPet, "Rabies", Date.valueOf(LocalDate.of(2023, 1, 1)), "John Doe", Date.valueOf(LocalDate.of(2024, 1, 1)));
        Vaccination vaccination2 = new Vaccination(testPet, "Distemper", Date.valueOf(LocalDate.of(2023, 2, 1)), "John Doe", Date.valueOf(LocalDate.of(2024, 2, 1)));
        Vaccination vaccination3 = new Vaccination(testPet, "Parvovirus", Date.valueOf(LocalDate.of(2023, 3, 1)), "John Doe", Date.valueOf(LocalDate.of(2024, 3, 1)));

        vaccinationRepository.save(vaccination1);
        vaccinationRepository.save(vaccination2);
        vaccinationRepository.save(vaccination3);
    }

    /**
     * Tests if the correct vaccination records are retrieved for a specific pet by its ID.
     * It asserts the size of the returned list and verifies the details of the vaccination records.
     */
    @Test
    public void testFindByPetId() {
        // Fetch vaccination records for the test pet by its ID
        List<Vaccination> vaccinationList = vaccinationRepository.findByPetId(testPet.getId());

        // Assert the size of the returned list matches the number of records created
        assertThat(vaccinationList).hasSize(3);

        // Assert the records' details are correct
        assertThat(vaccinationList.get(0).getVaccineName()).isEqualTo("Rabies");
        assertThat(vaccinationList.get(0).getAdministeredBy()).isEqualTo("John Doe");
        assertThat(vaccinationList.get(0).getVaccinationDate()).isEqualTo(Date.valueOf(LocalDate.of(2023, 1, 1)));
        assertThat(vaccinationList.get(0).getNextDueDate()).isEqualTo(Date.valueOf(LocalDate.of(2024, 1, 1)));

        assertThat(vaccinationList.get(1).getVaccineName()).isEqualTo("Distemper");
        assertThat(vaccinationList.get(1).getAdministeredBy()).isEqualTo("John Doe");
        assertThat(vaccinationList.get(1).getVaccinationDate()).isEqualTo(Date.valueOf(LocalDate.of(2023, 2, 1)));
        assertThat(vaccinationList.get(1).getNextDueDate()).isEqualTo(Date.valueOf(LocalDate.of(2024, 2, 1)));

        assertThat(vaccinationList.get(2).getVaccineName()).isEqualTo("Parvovirus");
        assertThat(vaccinationList.get(2).getAdministeredBy()).isEqualTo("John Doe");
        assertThat(vaccinationList.get(2).getVaccinationDate()).isEqualTo(Date.valueOf(LocalDate.of(2023, 3, 1)));
        assertThat(vaccinationList.get(2).getNextDueDate()).isEqualTo(Date.valueOf(LocalDate.of(2024, 3, 1)));
    }

    /**
     * Tests fetching vaccination records for a pet that doesn't exist.
     * Expects an empty list to be returned.
     */
    @Test
    public void testFindByPetId_NoRecords() {
        // Fetch vaccination records for a pet that doesn't exist (ID = 999L)
        List<Vaccination> vaccinationList = vaccinationRepository.findByPetId(999L);

        // Assert that the returned list is empty
        assertThat(vaccinationList).isEmpty();
    }
}
