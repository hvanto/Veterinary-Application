package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Custom query to find appointments by veterinarian and date
    List<Appointment> findByVeterinarianIdAndAppointmentDate(Long veterinarianId, Date appointmentDate);

    // Method to check if an appointment exists between a veterinarian and a pet
    boolean existsByVeterinarianAndPet(Veterinarian veterinarian, Pet pet);
}