package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.TreatmentPlan;
import au.edu.rmit.sept.webapp.repository.TreatmentPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing TreatmentPlan entities.
 * This service provides business logic related to the creation, retrieval,
 * and management of treatment plans for pets.
 */
@Service
public class TreatmentPlanService {

    // Autowired repository to handle CRUD operations for TreatmentPlan entities
    @Autowired
    private TreatmentPlanRepository treatmentPlanRepository;

    /**
     * Retrieves all treatment plans associated with a specific pet.
     *
     * @param petId The ID of the pet whose treatment plans are to be retrieved.
     * @return A list of TreatmentPlan objects associated with the specified pet.
     */
    public List<TreatmentPlan> getTreatmentPlansByPetId(Long petId) {
        return treatmentPlanRepository.findByPetId(petId);
    }

    /**
     * Saves a new treatment plan or updates an existing one in the database.
     *
     * @param plan The TreatmentPlan object to be saved.
     */
    public void save(TreatmentPlan plan) {
        treatmentPlanRepository.save(plan);
    }
}
