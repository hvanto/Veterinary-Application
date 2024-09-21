package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {
    List<TreatmentPlan> findByPetId(Long petId);
    List<TreatmentPlan> findByMedicalHistoryId(Long medicalHistoryId);
}
