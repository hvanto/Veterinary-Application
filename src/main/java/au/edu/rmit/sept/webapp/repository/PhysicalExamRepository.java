package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.PhysicalExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhysicalExamRepository extends JpaRepository<PhysicalExam, Long> {

    // Custom query to find all physical exams for a particular pet
    List<PhysicalExam> findByPetId(Long petId);
}
