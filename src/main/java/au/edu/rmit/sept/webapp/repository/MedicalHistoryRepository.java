package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.MedicalHistory;
import au.edu.rmit.sept.webapp.model.Pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {

    List<MedicalHistory> findByPetIdOrderByEventDateDesc(Long petId);

    // Fetch medical history by the prescription's id
    List<MedicalHistory> findByPrescriptionId(Long prescriptionId);

    @Query(value = "SELECT DISTINCT p FROM Pet p "
            + "JOIN medical_history mh ON p.id = mh.pet_id "
            + "WHERE mh.veterinarian_id = :vet_id", nativeQuery = true)
    List<Pet> findPetByVeterinarianId(@Param("vet_id") Long veterinarianId);
}
