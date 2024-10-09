package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.MedicalHistory;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.model.PrescriptionHistory;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.repository.PrescriptionRepository;
import au.edu.rmit.sept.webapp.repository.UserRepository;
import au.edu.rmit.sept.webapp.repository.VeterinarianRepository;
import au.edu.rmit.sept.webapp.service.MedicalHistoryService;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.VeterinarianService;
import au.edu.rmit.sept.webapp.repository.PrescriptionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PetService petService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;
    
    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionHistoryRepository prescriptionHistoryRepository;

    /**
     * Fetches all prescriptions. Seeds data if no prescriptions are found.
     * 
     * @return List of all prescriptions.
     */
    @GetMapping("/all")
    @ResponseBody
    public List<Prescription> getAllPrescriptions() {
        System.out.println("Fetching all prescriptions");

        // Get prescriptions from repository
        List<Prescription> prescriptions = prescriptionRepository.findAll();

        // If no prescriptions exist, seed data
        if (prescriptions.isEmpty()) {
            System.out.println("No prescriptions found. Seeding data...");
            seedPrescriptions();
            prescriptions = prescriptionRepository.findAll(); // Fetch again after seeding
        }

        return prescriptions;
    }

    /**
     * Fetches all prescription history records. Seeds data if none are found.
     * 
     * @return List of all prescription histories.
     */
    @GetMapping("/history/all")
    @ResponseBody
    public List<PrescriptionHistory> getAllPrescriptionHistories() {
        System.out.println("Fetching all prescription histories");

        // Get prescription history from repository
        List<PrescriptionHistory> histories = prescriptionHistoryRepository.findAll();

        // If no prescription histories exist, seed data
        if (histories.isEmpty()) {
            System.out.println("No prescription histories found. Seeding data...");
            seedPrescriptionHistories();
            histories = prescriptionHistoryRepository.findAll(); // Fetch again after seeding
        }

        return histories;
    }

    /**
     * Seeds default prescriptions into the database.
     */
    private void seedPrescriptions() {
        // Set dummy start and end dates
        Date startDate1 = new Date();
        Date endDate1 = new Date();

        Date startDate2 = new Date();
        Date endDate2 = new Date();

        // Create prescription records
        Prescription prescription1 = new Prescription(
                "Amoxicillin",
                "Dr. John Doe",
                "Dr. Sarah Lee",
                "250mg twice a day",
                startDate1,
                endDate1,
                "For bacterial infection");

        Prescription prescription2 = new Prescription(
                "Metronidazole",
                "Dr. Jane Smith",
                "Dr. Emily Brown",
                "500mg once a day",
                startDate2,
                endDate2,
                "For gastrointestinal issues");

        // Save the records to the database
        prescriptionRepository.save(prescription1);
        prescriptionRepository.save(prescription2);

        System.out.println("Prescriptions seeded.");
    }

    /**
     * Seeds default prescription history into the database.
     */
    private void seedPrescriptionHistories() {
        // Set dummy start and end dates
        Date startDate1 = new Date();
        Date endDate1 = new Date();

        Date startDate2 = new Date();
        Date endDate2 = new Date();

        // Create prescription history records
        PrescriptionHistory history1 = new PrescriptionHistory(
                "Dr. Alice Green",
                "Amoxicillin",
                "Vet A",
                "250mg twice a day",
                startDate1,
                endDate1,
                "Follow up in a week");

        PrescriptionHistory history2 = new PrescriptionHistory(
                "Dr. Bob White",
                "Metronidazole",
                "Vet B",
                "500mg once a day",
                startDate2,
                endDate2,
                "Monitor for side effects");

        // Save the records to the database
        prescriptionHistoryRepository.save(history1);
        prescriptionHistoryRepository.save(history2);

        System.out.println("Prescription histories seeded.");
    }

    /**
     * Adds a new prescription.
     * 
     * @param prescription The prescription to be added.
     * @return The added prescription.
     */
    @PostMapping("/add") // Updated mapping to "/add"
    @ResponseBody
    public Prescription addPrescription(@RequestBody Prescription prescription) {
        System.out.println("Adding prescription: " + prescription);
        return prescriptionRepository.save(prescription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id,
            @RequestBody Prescription updatedPrescription) {
        return prescriptionRepository.findById(id)
                .map(prescription -> {
                    // Update fields
                    prescription.setPractitioner(updatedPrescription.getPractitioner());
                    prescription.setPrescription(updatedPrescription.getPrescription());
                    prescription.setVet(updatedPrescription.getVet());
                    prescription.setDosage(updatedPrescription.getDosage());
                    prescription.setStartDate(updatedPrescription.getStartDate());
                    prescription.setEndDate(updatedPrescription.getEndDate());
                    prescription.setDescription(updatedPrescription.getDescription());
                    // prescription.setOrderTracking(updatedPrescription.getOrderTracking());

                    Prescription savedPrescription = prescriptionRepository.save(prescription);
                    return ResponseEntity.ok(savedPrescription);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a prescription.
     * 
     * @param id The ID of the prescription to be deleted.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        if (prescriptionRepository.existsById(id)) {
            prescriptionRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Return 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        }
    }

    /**
     * Fetches all pets associated with the logged-in vet.
     * This is used to display the pet selection screen.
     * @param vetId The ID of the logged-in vet.
     * @return A list of pets for which the vet has a medical history.
     */
    @GetMapping("/vet-pets")
    @ResponseBody
    public List<Pet> getVetPets(@RequestParam Long vetId) {
        System.out.println("Received request for pets with vetId: " + vetId);

        // Return empty list of vet does not exists
        if (!veterinarianRepository.existsById(vetId)) {
            return new ArrayList<Pet>();
        }

        List<Pet> pets = petService.getPetsByVeteterinarianId(vetId);
        // Only seed data if no pets exist for the vet
        if (pets.isEmpty()) {
            seedDataForVet(vetId);
            pets = petService.getPetsByVeteterinarianId(vetId);
        }
        return pets;
    }

    /**
     * Seeds default perscription or prescription history into the database for a vet.
     */
    private void seedDataForVet(Long vetId) {
        // Get vet
        Veterinarian vet = veterinarianRepository.findById(vetId).get();

        // Seed user
        User user = userRepository.save(new User("John", "Doe", "john.doe@example.com", "password123", "123456789"));
        
        // Seed pets
        Pet pet1 = new Pet(user, "Buddy", "Dog", "Golden Retriever", "Male", true, "Loves to play fetch", "2_buddy_retriever.png", LocalDate.of(2018, 1, 5));
        Pet pet2 = new Pet(user, "Luna", "Dog", "Siberian Husky", "Female", true, "Very energetic and loves snow", "3_luna_husky.png", LocalDate.of(2019, 3, 10));
        petService.save(pet1);
        petService.save(pet2);

        // Convert LocalDate to Date for medical history event dates
        Date historyDate1 = Date.from(LocalDate.of(2023, 1, 10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date historyDate2 = Date.from(LocalDate.of(2023, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Seed medical history
        MedicalHistory history1 = new MedicalHistory(pet1, vet.getFullName(), "Routine checkup", vet, historyDate1, "All good", null);
        MedicalHistory history2 = new MedicalHistory(pet2, vet.getFullName(), "Dental cleaning", vet, historyDate2, "Teeth cleaned", null);

        // Save the medical history records
        medicalHistoryService.save(history1);
        medicalHistoryService.save(history2);
        
        seedPrescriptions();
        seedPrescriptionHistories();
    }
}
