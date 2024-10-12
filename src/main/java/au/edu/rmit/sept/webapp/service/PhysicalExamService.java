package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.PhysicalExam;
import au.edu.rmit.sept.webapp.repository.PhysicalExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing PhysicalExam entities.
 * This service interacts with the PhysicalExamRepository to handle
 * business logic related to physical exams for pets.
 */
@Service
public class PhysicalExamService {

    // Autowire the repository to handle database operations for PhysicalExam
    @Autowired
    private PhysicalExamRepository physicalExamRepository;

    /**
     * Retrieves all physical exams associated with a specific pet.
     *
     * @param petId The ID of the pet whose physical exams are to be retrieved.
     * @return A list of PhysicalExam objects associated with the specified pet.
     */
    public List<PhysicalExam> getPhysicalExamsByPetId(Long petId) {
        return physicalExamRepository.findByPetId(petId);
    }

    /**
     * Saves a new physical exam or updates an existing one in the database.
     *
     * @param exam The PhysicalExam object to be saved.
     */
    public void save(PhysicalExam exam) {
        physicalExamRepository.save(exam);
    }
}