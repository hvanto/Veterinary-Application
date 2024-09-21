package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.WeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for WeightRecord.
 */
@Repository
public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {
    List<WeightRecord> findByPetId(Long petId);
}
