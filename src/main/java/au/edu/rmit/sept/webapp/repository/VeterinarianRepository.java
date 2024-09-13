package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {
    // Custom query methods if needed
}
