package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    // Custom query methods if needed
}
