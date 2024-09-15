package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {

    // Find all veterinarians by clinic_id
    List<Veterinarian> findAllByClinic_Id(long clinicId);

    // Find all veterinarians by service_id
    List<Veterinarian> findAllByServices_Id(long servicesId);

    // Find all veterinarians by clinic_id and services_id
    List<Veterinarian> findAllByClinic_IdAndServices_Id(Long clinic_id, Long services_id);
}
