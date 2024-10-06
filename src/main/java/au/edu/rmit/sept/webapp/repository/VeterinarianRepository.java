package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Service;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {

    // Find all veterinarians by clinic_id
    List<Veterinarian> findAllByClinic_Id(long clinicId);

    // Find all veterinarians by service_id
    List<Veterinarian> findAllByServices_Id(long servicesId);

    // Find all veterinarians by clinic_id and services_id
    List<Veterinarian> findAllByClinic_IdAndServices_Id(Long clinic_id, Long services_id);

    // Find all veterinarians by clinic ID
    List<Veterinarian> findAllByClinic_Id(Long clinicId);

    // Query to get veterinarian along with their services
    @Query("SELECT v FROM Veterinarian v JOIN FETCH v.services WHERE v.Id = :id")
    Optional<Veterinarian> findVeterinarianWithServicesById(Long id);

    // Find a veterinarian by email (for login)
    Optional<Veterinarian> findByEmail(String email);

    // Check if a veterinarian exists by email (for signup)
    boolean existsByEmail(String email);

    // Add a query to find veterinarian by email and clinic ID
    Optional<Veterinarian> findByEmailAndClinic_Id(String email, Long clinicId);
}
