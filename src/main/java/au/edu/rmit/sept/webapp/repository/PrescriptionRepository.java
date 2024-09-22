package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    // Custom query method to find prescriptions by petId
    List<Prescription> findByPetId(Long petId);

    List<Prescription> findCurrentPrescriptionsByPet(Pet pet);

    List<Prescription> findPrescriptionHistoryByPet(Pet pet);
}
