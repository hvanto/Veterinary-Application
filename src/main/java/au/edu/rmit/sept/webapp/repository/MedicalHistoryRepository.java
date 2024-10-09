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
}
