package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.Vaccination;
import au.edu.rmit.sept.webapp.repository.PetRepository;
import au.edu.rmit.sept.webapp.repository.UserRepository;
import au.edu.rmit.sept.webapp.repository.VaccinationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the VaccinationService class using a real MySQL database.
 */
@SpringBootTest
public class VaccinationServiceTests {

    @Autowired
    private VaccinationService vaccinationService;

    @Autowired
    private VaccinationRepository vaccinationRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    private Pet testPet;
    private Vaccination vaccination1;
    private Vaccination vaccination2;
    private User testUser;

    /**
     * Sets up test data before each test case runs.
     * Creates a test pet and vaccination records in the database.
     */
    @BeforeEach
    @Transactional
    public void setUp() {
        // Create and set up a test user
        testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        // Save the test user to the repository to persist and generate an ID
        testUser = userRepository.save(testUser);

        // Create and save a test pet
        testPet = new Pet();
        testPet.setName("Buddy");
        petRepository.save(testPet);

        // Create and save two vaccination records for the test pet
        vaccination1 = new Vaccination();
        vaccination1.setPet(testPet);
        vaccination1.setVaccineName("Rabies");
        vaccination1.setVaccinationDate(new Date());
        vaccination1.setAdministeredBy("Doctor Strange");  // Ensure this field is set properly
        vaccinationService.save(vaccination1);

        vaccination2 = new Vaccination();
        vaccination2.setPet(testPet);
        vaccination2.setVaccineName("Distemper");
        vaccination2.setVaccinationDate(new Date());
        vaccination2.setAdministeredBy("Doctor Strange");  // Ensure this field is set properly
        vaccinationService.save(vaccination2);
    }

    /**
     * Test for retrieving all vaccinations by pet ID.
     */
    @Test
    @Transactional
    public void testGetVaccinationsByPetId() {
        // Fetch vaccinations from the database using the service
        List<Vaccination> vaccinations = vaccinationService.getVaccinationsByPetId(testPet.getId());

        // Verify the results
        assertNotNull(vaccinations);
        assertEquals(2, vaccinations.size());
        assertEquals("Rabies", vaccinations.get(0).getVaccineName());
        assertEquals("Distemper", vaccinations.get(1).getVaccineName());
    }

    /**
     * Test for retrieving vaccinations by pet ID when no vaccinations exist.
     */
    @Test
    @Transactional
    public void testGetVaccinationsByPetId_Empty() {
        // Create a new pet with no vaccinations
        Pet newPet = new Pet();
        newPet.setName("Max");
        petRepository.save(newPet);

        // Fetch vaccinations for the new pet
        List<Vaccination> vaccinations = vaccinationService.getVaccinationsByPetId(newPet.getId());

        // Verify the result is an empty list
        assertNotNull(vaccinations);
        assertTrue(vaccinations.isEmpty());
    }

    /**
     * Test for saving a new vaccination.
     */
    @Test
    @Transactional
    public void testSaveNewVaccination() {
        // Create a new vaccination record
        Vaccination newVaccination = new Vaccination();
        newVaccination.setPet(testPet);
        newVaccination.setVaccineName("Parvovirus");
        newVaccination.setVaccinationDate(new Date());
        newVaccination.setAdministeredBy("Doctor Strange"); // Ensure the administeredBy field is set

        // Save the new vaccination using the service
        vaccinationService.save(newVaccination);

        // Fetch the saved vaccination from the database
        Optional<Vaccination> savedVaccination = vaccinationRepository.findById(newVaccination.getId());

        // Verify that the vaccination was saved correctly
        assertTrue(savedVaccination.isPresent());
        assertEquals("Parvovirus", savedVaccination.get().getVaccineName());
        assertEquals("Doctor Strange", savedVaccination.get().getAdministeredBy()); // Verify administeredBy
    }

    /**
     * Test for updating an existing vaccination.
     */
    @Test
    @Transactional
    public void testUpdateVaccination() {
        // Update vaccination1's name
        vaccination1.setVaccineName("Updated Rabies");
        vaccinationService.save(vaccination1);

        // Fetch the updated vaccination from the database
        Optional<Vaccination> updatedVaccination = vaccinationRepository.findById(vaccination1.getId());

        // Verify that the vaccination was updated correctly
        assertTrue(updatedVaccination.isPresent());
        assertEquals("Updated Rabies", updatedVaccination.get().getVaccineName());
    }
}