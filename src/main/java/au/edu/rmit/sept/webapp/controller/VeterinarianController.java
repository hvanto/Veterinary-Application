package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.*;
import au.edu.rmit.sept.webapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    // Get all veterinarians
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

    // Get veterinarians by service ID
    @PostMapping("/service/{serviceID}")
    public ResponseEntity<?> getVeterinariansByService(@PathVariable Long serviceID) {
        try {
            List<Veterinarian> veterinarians = veterinarianService.getVeterinariansByServiceId(serviceID);
            return ResponseEntity.ok(veterinarians);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get veterinarians by clinic ID & service ID
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
    public ResponseEntity<String> signup(@RequestBody Map<String, Object> signupData) {
        try {
            // Extract values from the request body
            String email = (String) signupData.get("email");
            String firstName = (String) signupData.get("firstName");
            String lastName = (String) signupData.get("lastName");
            String contact = (String) signupData.get("contact");
            String password = (String) signupData.get("password");
            String clinicName = (String) signupData.get("clinicName");

            // Check if the email already exists
            if (veterinarianService.emailExists(email)) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            // Create a new Veterinarian object
            Veterinarian veterinarian = new Veterinarian();
            veterinarian.setEmail(email);
            veterinarian.setFirstName(firstName);
            veterinarian.setLastName(lastName);
            veterinarian.setContact(contact);

            // Check if a clinic name is provided
            if (clinicName == null || clinicName.trim().isEmpty()) {
                // No clinic name provided, assign the "Independent" clinic
                Clinic defaultClinic = clinicService.getClinicByName("Independent");
                if (defaultClinic == null) {
                    return ResponseEntity.badRequest().body("Default clinic 'Independent' not found.");
                }
                veterinarian.setClinic(defaultClinic);
            } else {
                // Clinic name is provided, check if it exists in the database
                Clinic clinic = clinicService.getClinicByName(clinicName);
                if (clinic == null) {
                    return ResponseEntity.badRequest().body("Clinic not found. Please register the clinic first.");
                }
                veterinarian.setClinic(clinic);
            }

            // Set the password directly without encryption for now
            veterinarian.setPassword(password);

            // Save the veterinarian to the database
            veterinarianService.saveVeterinarian(veterinarian);

            // Return success message
            return ResponseEntity.ok("Veterinarian signed up successfully!");

        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error during signup: " + e.getMessage());
        }
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");
            String clinicName = loginRequest.get("clinicName"); // Clinic name can be optional

            // Validate veterinarian credentials (email, password, clinicName)
            Veterinarian veterinarian = veterinarianService.validateVeterinarianCredentials(email, password, clinicName);

            // Prepare a response map
            Map<String, Object> response = new HashMap<>();
            response.put("veterinarian", veterinarian);
            response.put("firstName", veterinarian.getFirstName());
            response.put("email", veterinarian.getEmail());
            response.put("redirectUrl", "/veterinarian-dashboard"); // Assuming Independent login

            // Return veterinarian data if successful
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Return error response if credentials are invalid
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Update veterinarian details
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Veterinarian> updateVeterinarian(@RequestBody Veterinarian updatedVeterinarian) {
        try {
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

    // Get veterinarians by clinic_id
    @PostMapping("/{veterinarianID}/appointments")
    public ResponseEntity<?> getAppointmentsByVeterinarian(@PathVariable Long veterinarianID) {
        try {
            List<Appointment> appointments = veterinarianService.getAppointmentsByVeterinarian(veterinarianID);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get veterinarians by clinic_id
    @PostMapping("/{veterinarianID}/clinic")
    public ResponseEntity<?> getClinicByVeterinarianId(@PathVariable Long veterinarianID) {
        try {
            Optional<Clinic> clinic = veterinarianService.getClinicByVeterinarianId(veterinarianID);
            return ResponseEntity.ok(clinic);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
