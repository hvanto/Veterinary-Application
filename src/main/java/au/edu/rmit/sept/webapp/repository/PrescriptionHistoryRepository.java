package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.PrescriptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionHistoryRepository extends JpaRepository<PrescriptionHistory, Long> {
    List<PrescriptionHistory> findByUserIdAndPetId(Long userId, Long petId);
}
