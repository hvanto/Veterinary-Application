package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on Pet entities.
 * Extends JpaRepository to provide built-in functionality like save, delete,
 * and find operations.
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    /**
     * Finds a pet by its ID.
     *
     * @param id The ID of the pet.
     * @return The Pet object if found, otherwise {@code null}.
     */
    Pet findById(long id); // Customize this if needed

    /**
     * Finds all pets associated with a specific user ID.
     *
     * @param userId The ID of the user.
     * @return List of Pet objects associated with the user.
     */
    List<Pet> findByUserId(Long userId);

    /**
     * Finds a pet by its name.
     *
     * @param name The name of the pet.
     * @return List of Pet objects with the specified name.
     */
    List<Pet> findByName(String name);

    /**
     * Finds all pets with a medical history with a specific vet ID
     *
     * @param vetId The ID of the vet.
     * @return List of Pet objects associated with the vet.
     */
    @Query(value = "SELECT DISTINCT p FROM Pet p "
            + "JOIN medical_history mh ON p.id = mh.pet_id "
            + "WHERE mh.veterinarian_id = :vet_id", nativeQuery = true)
    List<Pet> findByVeterinarianId(@Param("vet_id") Long veterinarianId);
}
