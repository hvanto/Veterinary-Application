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

/**
 * Controller for managing veterinarian-related actions.
 * Provides endpoints for retrieving, registering, updating, and managing veterinarians.
 */
@RestController
@RequestMapping("/api/veterinarian")
public class VeterinarianController {

    // Services injected to handle business logic
    private final VeterinarianService veterinarianService;
    private final ClinicService clinicService;

    @Autowired
    public VeterinarianController(VeterinarianService veterinarianService, ClinicService clinicService) {
        this.veterinarianService = veterinarianService;
        this.clinicService = clinicService;
    }

    /**
     * Retrieves all veterinarians from the database.
     *
     * @return A ResponseEntity containing a list of all veterinarians.
     */
    @PostMapping("/all")
    public ResponseEntity<?> getAllVeterinarians() {
        try {
            List<Veterinarian> veterinarians = veterinarianService.getAllVeterinarians();
            return ResponseEntity.ok(veterinarians);  // Return the list of veterinarians
        } catch (Exception e) {
            // Return error message if fetching fails
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Retrieves veterinarians by clinic ID.
     *
     * @param clinicID The ID of the clinic to filter veterinarians.
     * @return A ResponseEntity containing a list of veterinarians for the specified clinic.
     */
    @PostMapping("/clinic/{clinicID}")
    public ResponseEntity<?> getVeterinariansByClinic(@PathVariable Long clinicID) {
        try {
            List<Veterinarian> veterinarians = veterinarianService.getVeterinariansByClinicId(clinicID);
            return ResponseEntity.ok(veterinarians);  // Return the veterinarians in the specified clinic
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Retrieves veterinarians by service ID.
     *
     * @param serviceID The ID of the service provided by the veterinarians.
     * @return A ResponseEntity containing a list of veterinarians offering the specified service.
     */
    @PostMapping("/service/{serviceID}")
    public ResponseEntity<?> getVeterinariansByService(@PathVariable Long serviceID) {
        try {
            List<Veterinarian> veterinarians = veterinarianService.getVeterinariansByServiceId(serviceID);
            return ResponseEntity.ok(veterinarians);  // Return the veterinarians offering the specified service
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Retrieves veterinarians by both clinic ID and service ID.
     *
     * @param clinicID  The clinic ID to filter veterinarians.
     * @param serviceID The service ID to filter veterinarians.
     * @return A ResponseEntity containing veterinarians matching both clinic and service criteria.
     */
    @PostMapping("/clinic/{clinicID}/service/{serviceID}")
    public ResponseEntity<?> getVeterinariansByClinicAndService(@PathVariable Long clinicID, @PathVariable Long serviceID) {
        try {
            List<Veterinarian> veterinarians = veterinarianService.getVeterinariansByClinicIdAndServiceId(clinicID, serviceID);
            return ResponseEntity.ok(veterinarians);  // Return veterinarians by both clinic and service
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Retrieves services offered by a specific veterinarian.
     *
     * @param veterinarianId The ID of the veterinarian.
     * @return A ResponseEntity containing a list of services offered by the veterinarian.
     */
    @PostMapping("/{veterinarianId}/services")
    public ResponseEntity<?> getServicesByVeterinarianId(@PathVariable Long veterinarianId) {
        List<Service> services = veterinarianService.getServicesByVeterinarianId(veterinarianId);
        if (services != null) {
            return ResponseEntity.ok(services);  // Return the services offered by the veterinarian
        } else {
            return ResponseEntity.status(404).body("Veterinarian not found or no services available.");
        }
    }

    /**
     * Handles veterinarian signup.
     *
     * @param signupData The data required for signing up a veterinarian (email, name, contact, clinic, etc.).
     * @return Success or error message depending on the outcome of the signup process.
     */
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

            // Set the password directly with encryption for now
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

    /**
     * Handles veterinarian login.
     *
     * @param loginRequest The login credentials provided by the veterinarian (email, password, clinicName).
     * @return A map containing veterinarian details and redirection URL upon successful login.
     */
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

    /**
     * Updates veterinarian details.
     *
     * @param updatedVeterinarian The veterinarian object containing updated details.
     * @return The updated veterinarian details or an error message if the update fails.
     */
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Veterinarian> updateVeterinarian(@RequestBody Veterinarian updatedVeterinarian) {
        try {
            Veterinarian veterinarian = veterinarianService.updateVeterinarian(updatedVeterinarian);
            return ResponseEntity.ok(veterinarian);  // Return updated veterinarian details
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Handles password update for a veterinarian.
     *
     * @param veterinarian The veterinarian object containing the new password.
     * @return A success or error message depending on the outcome of the update.
     */
    @PutMapping(value = "/updatePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody Veterinarian veterinarian) {
        Map<String, String> response = new HashMap<>();
        try {
            veterinarianService.updatePassword(veterinarian.getId(), veterinarian.getPassword());
            response.put("message", "Password updated successfully!");
            return ResponseEntity.ok(response);  // Return success message
        } catch (Exception e) {
            response.put("error", "Failed to update password: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Retrieves appointments for a specific veterinarian.
     *
     * @param veterinarianID The ID of the veterinarian whose appointments are to be fetched.
     * @return A ResponseEntity containing the appointments for the veterinarian.
     */
    @PostMapping("/{veterinarianID}/appointments")
    public ResponseEntity<?> getAppointmentsByVeterinarian(@PathVariable Long veterinarianID) {
        try {
            List<Appointment> appointments = veterinarianService.getAppointmentsByVeterinarian(veterinarianID);
            return ResponseEntity.ok(appointments);  // Return appointments for the veterinarian
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
