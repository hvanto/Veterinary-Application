package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.PetRepository;
import au.edu.rmit.sept.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PetService class. This class tests various methods in PetService
 * to ensure that pets are managed correctly, including CRUD operations.
 */
@SpringBootTest
public class PetServiceTests {

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    private Pet testPet;
    private User testUser;

    /**
     * This method is executed before each test. It sets up a test user and a test pet
     * and saves them to the repository to provide test data for each test case.
     */
    @BeforeEach
    public void setUp() {
        // Create and set up a test user
        testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        // Save the test user to the repository to persist and generate an ID
        testUser = userRepository.save(testUser);

        // Create a test pet and associate it with the saved user
        testPet = new Pet();
        testPet.setName("TestPet");
        testPet.setUser(testUser);

        // Save the test pet to the repository
        petRepository.save(testPet);
    }

    /**
     * Test case for retrieving a pet by its ID when the pet exists.
     * This test ensures that a pet can be fetched by its ID successfully.
     */
    @Test
    public void testGetPetById_PetExists() {
        // Fetch the pet from the database using the saved testPet's ID
        Pet pet = petService.getPetById(testPet.getId());

        // Verify the fetched pet is not null and matches the expected pet
        assertNotNull(pet);
        assertEquals("TestPet", pet.getName());
    }

    /**
     * Test case for retrieving a pet by its ID when the pet does not exist.
     * This test ensures that the service returns null when a non-existing pet is fetched.
     */
    @Test
    public void testGetPetById_PetNotFound() {
        // Call the service method with a non-existing ID (999L)
        Pet pet = petService.getPetById(999L);

        // Verify that the service returns null for a non-existent pet
        assertNull(pet);
    }

    /**
     * Test case for retrieving all pets associated with a specific user ID.
     * This test ensures that the correct pets are fetched for a given user.
     */
    @Test
    public void testGetPetsByUserId() {
        // Fetch the list of pets associated with the test user
        List<Pet> pets = petService.getPetsByUserId(testUser.getId());

        // Verify the list contains the correct number of pets and their details
        assertNotNull(pets);
        assertEquals(1, pets.size());
        assertEquals("TestPet", pets.get(0).getName());
    }

    /**
     * Test case for saving a new pet.
     * This test ensures that a new pet can be successfully saved to the database.
     */
    @Test
    public void testSavePet_NewPet() {
        // Create a new pet and associate it with the test user
        Pet newPet = new Pet();
        newPet.setName("Charlie");
        newPet.setUser(testUser);

        // Save the new pet
        petService.save(newPet);

        // Verify that the new pet was saved successfully
        Optional<Pet> savedPet = petRepository.findById(newPet.getId());
        assertTrue(savedPet.isPresent());
        assertEquals("Charlie", savedPet.get().getName());
    }

    /**
     * Test case for deleting a pet by its ID.
     * This test ensures that a pet can be successfully deleted from the database.
     */
    @Test
    public void testDeletePet() {
        // Delete the test pet using its ID
        petService.delete(testPet.getId());

        // Verify that the pet has been successfully deleted
        Optional<Pet> deletedPet = petRepository.findById(testPet.getId());
        assertFalse(deletedPet.isPresent());
    }

    /**
     * Test case for retrieving all pets associated with a specific veterinarian ID.
     * This test assumes that no pets are associated with the given veterinarian ID (1L)
     * and verifies the result.
     */
    @Test
    public void testGetPetsByVeterinarianId() {
        // Fetch the list of pets associated with veterinarian ID 1L
        List<Pet> pets = petService.getPetsByVeteterinarianId(1L);

        // Verify that the list is not null and contains no pets (assuming no pets are linked to this veterinarian ID)
        assertNotNull(pets);
        assertEquals(0, pets.size());
    }
}
