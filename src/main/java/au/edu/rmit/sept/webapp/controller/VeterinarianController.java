package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.model.Service;
import au.edu.rmit.sept.webapp.service.ClinicService;
import au.edu.rmit.sept.webapp.service.VeterinarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import au.edu.rmit.sept.webapp.model.Clinic;

import java.util.HashMap;
import java.util.Map;

import java.util.List;


@RestController
@RequestMapping("/api/veterinarian")
public class VeterinarianController {
    private final VeterinarianService veterinarianService;
    private final ClinicService clinicService;

    @Autowired
    public VeterinarianController(VeterinarianService veterinarianService, ClinicService clinicService) {
        this.veterinarianService = veterinarianService;
        this.clinicService = clinicService;
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

    // Get all services by veterinarian ID
    @PostMapping("/{veterinarianId}/services")
    public ResponseEntity<?> getServicesByVeterinarianId(@PathVariable Long veterinarianId) {
        List<Service> services = veterinarianService.getServicesByVeterinarianId(veterinarianId);
        if (services != null) {
            return ResponseEntity.ok(services);
        } else {
            return ResponseEntity.status(404).body("Veterinarian not found or no services available.");
        }
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signup(@RequestBody Veterinarian veterinarian) {
        try {
            // Check if the email already exists
            if (veterinarianService.emailExists(veterinarian.getEmail())) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            // If no clinic is provided, assign the "Independent" clinic
            if (veterinarian.getClinic() == null || veterinarian.getClinic().getName() == null || veterinarian.getClinic().getName().isEmpty()) {
                Clinic defaultClinic = clinicService.getClinicByName("Independent");
                veterinarian.setClinic(defaultClinic);
            } else {
                // If a clinic name is provided, validate the clinic
                Clinic clinic = clinicService.getClinicByName(veterinarian.getClinic().getName());
                if (clinic == null) {
                    return ResponseEntity.badRequest().body("Clinic not found");
                }
                veterinarian.setClinic(clinic);
            }

            // Save the veterinarian to the database
            veterinarianService.saveVeterinarian(veterinarian);
            return ResponseEntity.ok("Veterinarian signed up successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Veterinarian> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");
            String clinicIdStr = loginRequest.get("clinicId");

            // Try to parse clinicId if provided, otherwise set it to null
            Long clinicId = null;
            if (clinicIdStr != null && !clinicIdStr.isEmpty()) {
                clinicId = Long.parseLong(clinicIdStr);
            }

            // Validate veterinarian credentials using the clinic ID (if provided)
            Veterinarian veterinarian = veterinarianService.validateVeterinarianCredentials(email, password, clinicId);

            // If the veterinarian belongs to the "Independent" clinic, clinic ID is not required
            if (veterinarian.getClinic() != null && veterinarian.getClinic().getName().equals("Independent")) {
                return ResponseEntity.ok(veterinarian);
            } else {
                // For other clinics, clinicId must be provided and must match the veterinarian's clinic
                if (clinicId == null || !veterinarian.getClinic().getId().equals(clinicId)) {
                    throw new Exception("Clinic ID is required and must match the veterinarian's assigned clinic.");
                }
                return ResponseEntity.ok(veterinarian);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Update veterinarian details in the database
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Veterinarian> updateVeterinarian(@RequestBody Veterinarian updatedVeterinarian) {
        try {
            // Update veterinarian details in the database
            Veterinarian veterinarian = veterinarianService.updateVeterinarian(updatedVeterinarian);
            return ResponseEntity.ok(veterinarian);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Handle password update
    @PutMapping(value = "/updatePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody Veterinarian veterinarian) {
        Map<String, String> response = new HashMap<>();
        try {
            veterinarianService.updatePassword(veterinarian.getId(), veterinarian.getPassword());
            response.put("message", "Password updated successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to update password: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

