package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.*;
import au.edu.rmit.sept.webapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/veterinarian")
public class VeterinarianController {

    private final VeterinarianService veterinarianService;
    private final ClinicService clinicService;
    private final PetService petService;
    private final AppointmentService appointmentService;
    private final MedicalHistoryService medicalHistoryService;
    private final TreatmentPlanService treatmentPlanService;
    private final VaccinationService vaccinationService;
    private final PhysicalExamService physicalExamService;
    private final UserService userService;

    @Autowired
    public VeterinarianController(VeterinarianService veterinarianService, ClinicService clinicService,
                                  PetService petService, AppointmentService appointmentService,
                                  MedicalHistoryService medicalHistoryService,
                                  TreatmentPlanService treatmentPlanService,
                                  VaccinationService vaccinationService,
                                  PhysicalExamService physicalExamService,
                                  UserService userService) {
        this.veterinarianService = veterinarianService;
        this.clinicService = clinicService;
        this.petService = petService;
        this.appointmentService = appointmentService;
        this.medicalHistoryService = medicalHistoryService;
        this.treatmentPlanService = treatmentPlanService;
        this.vaccinationService = vaccinationService;
        this.physicalExamService = physicalExamService;
        this.userService = userService;
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

    @GetMapping("/veterinarian-upload-records")
    public ResponseEntity<?> veterinarianUploadRecords(@RequestParam("appointmentId") Long appointmentId) {
        try {
            // Fetch the appointment by ID
            System.out.println("Fetching appointment with ID: " + appointmentId);
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);

            // Log if the appointment is null
            if (appointment == null) {
                System.out.println("Appointment not found");
                throw new Exception("Appointment not found");
            }

            // Fetch the associated pet from the appointment
            Pet pet = appointment.getPet();

            // Fetch veterinarian associated with the appointment
            Veterinarian veterinarian = appointment.getVeterinarian();

            // Log to check pet and veterinarian existence
            System.out.println("Appointment found: " + appointment);
            System.out.println("Associated pet: " + appointment.getPet());
            System.out.println("Associated veterinarian: " + appointment.getVeterinarian());

            // Build a response map to send relevant data back to the frontend
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("appointment", appointment);
            responseData.put("pet", pet);
            responseData.put("veterinarian", veterinarian);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error loading data: " + e.getMessage());
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

    @PostMapping("/veterinarian/upload-records")
    public ResponseEntity<?> uploadMedicalRecord(@RequestParam("appointmentId") Long appointmentId,
                                                 @RequestParam("description") String description,
                                                 @RequestParam("file") MultipartFile file,
                                                 @RequestParam("category") String category,
                                                 Principal principal) {
        try {
            // Fetch the veterinarian by their username (email)
            Veterinarian veterinarian = veterinarianService.findByEmail(principal.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Veterinarian not found"));

            // Fetch the appointment and pet
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            if (appointment == null) {
                return ResponseEntity.badRequest().body("Appointment not found with ID: " + appointmentId);
            }

            Pet pet = appointment.getPet();
            if (pet == null) {
                return ResponseEntity.badRequest().body("Pet not found for this appointment");
            }

            // Create and save data based on the selected category
            switch (category.toLowerCase()) {
                case "treatment-plan":
                    TreatmentPlan treatmentPlan = new TreatmentPlan(
                            pet,
                            convertToLocalDateViaInstant(new Date()), // Convert to LocalDate
                            description,
                            veterinarian.getFullName(),
                            description,
                            null
                    );
                    treatmentPlanService.save(treatmentPlan);
                    break;
                case "vaccination":
                    Vaccination vaccination = new Vaccination(
                            pet,
                            description,
                            convertToDateViaInstant(convertToLocalDateViaInstant(new Date())), // Convert LocalDate to Date
                            veterinarian.getFullName(),
                            convertToDateViaInstant(convertToLocalDateViaInstant(new Date())) // Convert LocalDate to Date
                    );
                    vaccinationService.save(vaccination);
                    break;
                case "physical-exam":
                    PhysicalExam physicalExam = new PhysicalExam(
                            pet,
                            convertToLocalDateViaInstant(new Date()), // Convert to LocalDate
                            veterinarian.getFullName(),
                            description
                    );
                    physicalExamService.save(physicalExam);
                    break;
                case "weight-record":
                    // Handle weight record if necessary
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid category");
            }

            return ResponseEntity.ok("Medical record uploaded successfully");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading medical record: " + e.getMessage());
        }
    }

    // Helper method to convert Date to LocalDate
    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    // Helper method to convert LocalDate to Date
    private Date convertToDateViaInstant(LocalDate localDateToConvert) {
        return Date.from(localDateToConvert.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
