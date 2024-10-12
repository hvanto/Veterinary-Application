package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.MedicalHistory;
import au.edu.rmit.sept.webapp.repository.MedicalHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing MedicalHistory entities.
 * This service provides methods to interact with the MedicalHistoryRepository,
 * facilitating business logic related to retrieving and saving medical history data.
 */
@Service
public class MedicalHistoryService {

    // Autowired instance of MedicalHistoryRepository to perform database operations
    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    /**
     * Retrieves the medical history of a specific pet by its ID.
     * The medical history is ordered by event date in descending order.
     *
     * @param petId The ID of the pet whose medical history is to be fetched.
     * @return A list of MedicalHistory objects associated with the specified pet.
     */
    public List<MedicalHistory> getMedicalHistoryByPetId(Long petId) {
        return medicalHistoryRepository.findByPetIdOrderByEventDateDesc(petId);
    }

    /**
     * Saves a MedicalHistory record to the database.
     * If the record already exists, it will be updated.
     *
     * @param medicalHistory The MedicalHistory object to be saved.
     */
    public void save(MedicalHistory medicalHistory) {
        medicalHistoryRepository.save(medicalHistory);
    }
}
