package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.VeterinarianAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeterinarianAvailabilityRepository extends JpaRepository<VeterinarianAvailability, Long> {

    // Find all availability records by veterinarian ID
    List<VeterinarianAvailability> findAllByVeterinarian_Id(Long veterinarianId);
}
