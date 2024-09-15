package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.service.VeterinarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/veterinarian")
public class VeterinarianController {
    private final VeterinarianService veterinarianService;

    @Autowired
    public VeterinarianController(VeterinarianService veterinarianService) {
        this.veterinarianService = veterinarianService;
    }

    // Get all services
    @PostMapping("/all")
    public ResponseEntity<?> getAllVeterinarians() {
        try {
            List<Veterinarian> veterinarians = veterinarianService.getAllVeterinarians();

            return ResponseEntity.ok(veterinarians);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get veterinarians by clinic_id
    @PostMapping("/clinic/{clinicID}")
    public ResponseEntity<?> getVeterinariansByClinic(@PathVariable Long clinicID) {
        try {
            List<Veterinarian> veterinarians = veterinarianService.getVeterinariansByClinicId(clinicID);
            return ResponseEntity.ok(veterinarians);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all veterinarians by service ID
    @PostMapping("/service/{serviceID}")
    public ResponseEntity<?> getVeterinariansByService(@PathVariable Long serviceID) {
        try {
            List<Veterinarian> veterinarians = veterinarianService.getVeterinariansByServiceId(serviceID);
            return ResponseEntity.ok(veterinarians);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all veterinarians by clinic ID & service ID
    @PostMapping("/clinic/{clinicID}/service/{serviceID}")
    public ResponseEntity<?> getVeterinariansByClinicAndService(@PathVariable Long clinicID, @PathVariable Long serviceID) {
        try {
            List<Veterinarian> veterinarians = veterinarianService.getVeterinariansByClinicIdAndServiceId(clinicID, serviceID);
            return ResponseEntity.ok(veterinarians);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
