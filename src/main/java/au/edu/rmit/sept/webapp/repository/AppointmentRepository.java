package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Custom query to find appointments by veterinarian and date
    List<Appointment> findByVeterinarianIdAndAppointmentDate(Long veterinarianId, Date appointmentDate);

    // Method to check if an appointment exists between a veterinarian and a pet
    boolean existsByVeterinarianAndPet(Veterinarian veterinarian, Pet pet);

    @Query("SELECT a FROM Appointment a WHERE a.veterinarian.id = :veterinarianId AND a.appointmentDate >= :today")
    List<Appointment> findAllByVeterinarianWithDateOnOrAfter(@Param("veterinarianId") Long veterinarianId, @Param("today") Date today);

    @Query("SELECT a FROM Appointment a WHERE a.user.id = :userId")
    List<Appointment> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT a.veterinarian FROM Appointment a WHERE a.id = :appointmentId")
    Veterinarian getVeterinarianByAppointmentId(Long appointmentId);

    @Query("SELECT a.pet FROM Appointment a WHERE a.id = :appointmentId")
    Pet getPetByAppointmentId(Long appointmentId);
}