package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for handling CRUD operations on MedicalHistory entities.
 * This interface extends JpaRepository, which provides various methods for
 * interacting with the database.
 */
@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {

    /**
     * Retrieves a list of MedicalHistory records for a specific pet, ordered by
     * event date in descending order.
     *
     * @param petId The ID of the pet whose medical history is being retrieved.
     * @return A list of MedicalHistory records ordered by the event date in descending order.
     */
    List<MedicalHistory> findByPetIdOrderByEventDateDesc(Long petId);
}
