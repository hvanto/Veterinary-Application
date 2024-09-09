package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that provides business logic for Pet entities.
 * This class interacts with the PetRepository to retrieve pet data from the database.
 */
@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    /**
     * Retrieves a pet from the database by its ID.
     *
     * @param petId The ID of the pet to retrieve.
     * @return The Pet object if found, otherwise {@code null}.
     */
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId).orElse(null);
    }

    /**
     * Retrieves all pets associated with a specific user.
     *
     * @param userId The ID of the user whose pets to retrieve.
     * @return A list of Pet objects associated with the user.
     */
    public List<Pet> getPetsByUserId(Long userId) {
        return petRepository.findByUserId(userId);
    }

    /**
     * Retrieves all pets by their name.
     *
     * @param name The name of the pet.
     * @return A list of Pet objects with the specified name.
     */
    public List<Pet> getPetsByName(String name) {
        return petRepository.findByName(name);
    }
}
