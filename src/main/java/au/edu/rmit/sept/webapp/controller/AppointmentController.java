package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(
            @RequestParam String day,
            @RequestParam String year,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam Long user,
            @RequestParam Long veterinarian,
            @RequestParam Long pet,
            @RequestParam(required = false) String notes) {
        try {
            Appointment appointment = appointmentService.createAppointment(day, year, startTime, endTime, user, veterinarian, pet, notes);
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // POST request to get appointments by veterinarian and day using path variables
    @PostMapping("/veterinarian/{veterinarianId}/day/{day}/year/{year}")
    public ResponseEntity<List<Appointment>> getAppointmentsByVeterinarianAndDay(
            @PathVariable Long veterinarianId,
            @PathVariable String day,
            @PathVariable String year) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByVeterinarianAndDay(veterinarianId, day, year);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // POST request to get appointments by veterinarian and day using path variables
    @PostMapping("/user/{userId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByVeterinarianAndDay(
            @PathVariable Long userId) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByUser(userId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{appointmentId}/veterinarian")
    public ResponseEntity<Veterinarian> getVeterinarianByAppointmentId(
            @PathVariable Long appointmentId) {
        try {
            Veterinarian veterinarian = appointmentService.getAppointmentVeterinarian(appointmentId);
            return ResponseEntity.ok(veterinarian);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{appointmentId}/pet")
    public ResponseEntity<Pet> getPetByAppointmentId(
            @PathVariable Long appointmentId) {
        try {
            Pet pet = appointmentService.getAppointmentPet(appointmentId);
            return ResponseEntity.ok(pet);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
