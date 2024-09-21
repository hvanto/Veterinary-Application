package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for MedicalHistory.
 * Extends JpaRepository to provide CRUD operations and custom queries.
 */
@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {

    /**
     * Finds the list of medical history records associated with a specific pet, ordered by event date.
     *
     * @param petId ID of the pet whose medical history is to be retrieved.
     * @return List of MedicalHistory objects associated with the specified pet, ordered by event date.
     */
    List<MedicalHistory> findByPetIdOrderByEventDateDesc(Long petId);
    // Fetch medical history by prescriptionId if needed
    List<MedicalHistory> findByPrescriptionId(Long prescriptionId);
}
