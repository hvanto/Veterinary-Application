package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.WeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for handling CRUD operations on WeightRecord entities.
 * This interface extends JpaRepository, providing methods to interact with the database.
 */
@Repository
public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {

    /**
     * Retrieves a list of all WeightRecord entries for a specific pet by its pet ID.
     *
     * @param petId The ID of the pet whose weight records are being retrieved.
     * @return A list of WeightRecord entries associated with the specified pet ID.
     */
    List<WeightRecord> findByPetId(Long petId);
}
