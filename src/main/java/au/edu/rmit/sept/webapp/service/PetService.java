package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Pet entities.
 * This service interacts with the PetRepository to handle business logic
 * related to pet management, such as retrieving, saving, and deleting pets.
 */
@Service
public class PetService {

    // Autowire the PetRepository to interact with the database
    @Autowired
    private PetRepository petRepository;

    /**
     * Fetches a pet by its ID.
     *
     * @param petId The ID of the pet to be retrieved.
     * @return The Pet object if found, or null if not found.
     */
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId).orElse(null);
    }

    /**
     * Retrieves all pets associated with a specific user.
     *
     * @param userId The ID of the user whose pets are to be retrieved.
     * @return A list of Pet objects belonging to the user.
     */
    public List<Pet> getPetsByUserId(Long userId) {
        return petRepository.findByUserId(userId);
    }

    /**
     * Saves a new pet or updates an existing pet in the database.
     *
     * @param pet The Pet object to be saved.
     */
    public void save(Pet pet) {
        petRepository.save(pet);
    }

    /**
     * Deletes a pet from the database by its ID.
     *
     * @param petId The ID of the pet to be deleted.
     */
    public void delete(Long petId) {
        petRepository.deleteById(petId);
    }

    /**
     * Gets pet associated to the veterinarianId
     *
     * @param veterinarianId The ID of the vet. 
     */
    public List<Pet> getPetsByVeteterinarianId(Long veterinarianId) {
        return petRepository.findByVeterinarianId(veterinarianId);
    }
}
