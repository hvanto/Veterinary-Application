package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.PhysicalExam;
import au.edu.rmit.sept.webapp.repository.PhysicalExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhysicalExamService {

    @Autowired
    private PhysicalExamRepository physicalExamRepository;

    // Fetch all physical exams
    public List<PhysicalExam> getAllPhysicalExams() {
        return physicalExamRepository.findAll();
    }

    // Fetch physical exams by pet ID
    public List<PhysicalExam> getPhysicalExamsByPetId(Long petId) {
        return physicalExamRepository.findByPetId(petId);
    }

    // Fetch a physical exam by ID
    public PhysicalExam getPhysicalExamById(Long id) {
        return physicalExamRepository.findById(id).orElse(null);
    }

    // Save a new or updated physical exam
    public PhysicalExam savePhysicalExam(PhysicalExam physicalExam) {
        return physicalExamRepository.save(physicalExam);
    }

    // Delete a physical exam by ID
    public void deletePhysicalExam(Long id) {
        physicalExamRepository.deleteById(id);
    }
}
