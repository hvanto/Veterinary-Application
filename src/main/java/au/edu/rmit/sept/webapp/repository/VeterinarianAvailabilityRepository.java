package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.VeterinarianAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarianAvailabilityRepository extends JpaRepository<VeterinarianAvailability, Long> {
    // Custom query methods if needed
}
