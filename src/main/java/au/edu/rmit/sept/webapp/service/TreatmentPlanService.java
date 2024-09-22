package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.TreatmentPlan;
import au.edu.rmit.sept.webapp.repository.TreatmentPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TreatmentPlanService {

    @Autowired
    private TreatmentPlanRepository treatmentPlanRepository;

    public List<TreatmentPlan> getTreatmentPlansByPetId(Long petId) {
        return treatmentPlanRepository.findByPetId(petId);
    }

    public Optional<TreatmentPlan> getTreatmentPlanById(Long id) {
        return treatmentPlanRepository.findById(id);
    }

    public void save(TreatmentPlan plan) {
        treatmentPlanRepository.save(plan);
    }

    public void deleteTreatmentPlan(Long id) {
        treatmentPlanRepository.deleteById(id);
    }
}
