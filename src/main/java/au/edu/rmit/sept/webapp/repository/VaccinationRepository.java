package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for handling CRUD operations on Vaccination entities.
 * This interface extends JpaRepository, providing methods to interact with the database.
 */
@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {

    /**
     * Retrieves a list of all Vaccination records for a specific pet by its pet ID.
     *
     * @param petId The ID of the pet whose vaccination records are being retrieved.
     * @return A list of Vaccination records associated with the specified pet ID.
     */
    List<Vaccination> findByPetId(Long petId);
}
