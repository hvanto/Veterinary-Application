package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.WeightRecord;
import au.edu.rmit.sept.webapp.repository.WeightRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeightRecordService {

    @Autowired
    private WeightRecordRepository weightRecordRepository;

    public List<WeightRecord> getWeightRecordsByPetId(Long petId) {
        return weightRecordRepository.findByPetId(petId);
    }

    public void save(WeightRecord weight) {
        weightRecordRepository.save(weight);
    }
}
