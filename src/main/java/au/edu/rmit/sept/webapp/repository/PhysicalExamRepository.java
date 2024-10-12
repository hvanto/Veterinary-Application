package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.PhysicalExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for handling CRUD operations on PhysicalExam entities.
 * This interface extends JpaRepository, providing methods for interacting with the database.
 */
@Repository
public interface PhysicalExamRepository extends JpaRepository<PhysicalExam, Long> {

    /**
     * Retrieves a list of all PhysicalExam records for a specific pet by its pet ID.
     *
     * @param petId The ID of the pet whose physical exams are being retrieved.
     * @return A list of PhysicalExam records associated with the specified pet ID.
     */
    List<PhysicalExam> findByPetId(Long petId);
}
