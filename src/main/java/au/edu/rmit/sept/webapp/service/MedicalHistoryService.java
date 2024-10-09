package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.MedicalHistory;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.repository.MedicalHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service layer for managing MedicalHistory.
 * This service interacts with the repository layer to fetch medical history data.
 */
@Service
public class MedicalHistoryService {

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    /**
     * Retrieves the medical history for a specific pet.
     *
     * @param petId The ID of the pet whose medical history is to be retrieved.
     * @return A list of MedicalHistory objects associated with the specified pet.
     */
    public List<MedicalHistory> getMedicalHistoryByPetId(Long petId) {
        return medicalHistoryRepository.findByPetIdOrderByEventDateDesc(petId);
    }

    public void save(MedicalHistory history4) {
        medicalHistoryRepository.save(history4);
    }
}
