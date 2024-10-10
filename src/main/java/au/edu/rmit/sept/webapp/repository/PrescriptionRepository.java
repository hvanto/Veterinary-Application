package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    /**
     * Find prescriptions by user ID and pet ID.
     *
     * @param userId ID of the user.
     * @param petId  ID of the pet.
     * @return List of prescriptions matching the user and pet IDs.
     */
    List<Prescription> findByUserIdAndPetId(Long userId, Long petId);
}
