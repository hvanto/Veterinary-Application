package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {

    List<Vaccination> findByPetId(Long petId);
}
