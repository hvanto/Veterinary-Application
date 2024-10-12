package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.WeightRecord;
import au.edu.rmit.sept.webapp.repository.WeightRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing WeightRecord entities.
 * This service provides the business logic for retrieving and saving weight records
 * associated with pets.
 */
@Service
public class WeightRecordService {

    // Injects the WeightRecordRepository to interact with the database
    @Autowired
    private WeightRecordRepository weightRecordRepository;

    /**
     * Retrieves a list of weight records for a specific pet by its ID.
     *
     * @param petId The ID of the pet whose weight records are to be retrieved.
     * @return A list of WeightRecord objects associated with the specified pet.
     */
    public List<WeightRecord> getWeightRecordsByPetId(Long petId) {
        return weightRecordRepository.findByPetId(petId);
    }

    /**
     * Saves a new weight record or updates an existing one in the database.
     *
     * @param weight The WeightRecord object to be saved.
     */
    public void save(WeightRecord weight) {
        weightRecordRepository.save(weight);
    }
}
