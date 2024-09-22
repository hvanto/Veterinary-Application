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

    public List<PhysicalExam> getAllPhysicalExams() {
        return physicalExamRepository.findAll();
    }

    public List<PhysicalExam> getPhysicalExamsByPetId(Long petId) {
        return physicalExamRepository.findByPetId(petId);
    }

    public PhysicalExam getPhysicalExamById(Long id) {
        return physicalExamRepository.findById(id).orElse(null);
    }

    public void save(PhysicalExam exam) {
        physicalExamRepository.save(exam);
    }

    public void deletePhysicalExam(Long id) {
        physicalExamRepository.deleteById(id);
    }
}
