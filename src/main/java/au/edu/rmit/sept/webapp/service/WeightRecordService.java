package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.WeightRecord;
import au.edu.rmit.sept.webapp.repository.WeightRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class to manage weight records for pets.
 */
@Service
public class WeightRecordService {

    @Autowired
    private WeightRecordRepository weightRecordRepository;

    /**
     * Retrieves the weight history for a specific pet.
     *
     * @param petId The ID of the pet.
     * @return A list of WeightRecord objects for the specified pet.
     */
    public List<WeightRecord> getWeightRecordsByPetId(Long petId) {
        return weightRecordRepository.findByPetId(petId);
    }

    /**
     * Adds a new weight record for a pet.
     *
     * @param weightRecord The WeightRecord object to be saved.
     * @return The saved WeightRecord object.
     */
    public WeightRecord addWeightRecord(WeightRecord weightRecord) {
        return weightRecordRepository.save(weightRecord);
    }
}
