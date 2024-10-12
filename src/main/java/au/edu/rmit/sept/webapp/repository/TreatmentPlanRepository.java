package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for handling CRUD operations on TreatmentPlan entities.
 * This interface extends JpaRepository, providing methods for interacting with the database.
 */
@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {

    /**
     * Retrieves a list of all TreatmentPlan records for a specific pet by its pet ID.
     *
     * @param petId The ID of the pet whose treatment plans are being retrieved.
     * @return A list of TreatmentPlan records associated with the specified pet ID.
     */
    List<TreatmentPlan> findByPetId(Long petId);
}
