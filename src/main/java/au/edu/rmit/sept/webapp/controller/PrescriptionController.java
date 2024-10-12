package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.*;
import au.edu.rmit.sept.webapp.repository.*;
import au.edu.rmit.sept.webapp.service.MedicalHistoryService;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionHistoryRepository prescriptionHistoryRepository;

    @Autowired
    private RefillRepository refillRepository;

    /**
     * Fetches all prescriptions. Seeds data if no prescriptions are found.
     * 
     * @return List of all prescriptions of a user.
     */
    @GetMapping("/all")
    @ResponseBody
    public List<Prescription> getAllPrescriptions(@RequestParam Long petId) {
        System.out.println("Fetching all prescriptions");

        Pet pet = petService.getPetById(petId);

        // Get prescriptions from repository
        List<Prescription> prescriptions = prescriptionRepository.findByPet(pet);

        // If no prescriptions exist, seed data
        if (prescriptions.isEmpty()) {
            System.out.println("No prescriptions found. Seeding data...");
            seedPrescriptions(petId);
            prescriptions = prescriptionRepository.findByPet(pet); // Fetch again after seeding
        }

        return prescriptions;
    }

    /**
     * Fetches all prescription history records. Seeds data if none are found.
     * 
     * @return List of all prescription histories of a user.
     */
    @GetMapping("/history/all")
    @ResponseBody
    public List<PrescriptionHistory> getAllPrescriptionHistories(@RequestParam Long petId) {
        System.out.println("Fetching all prescription histories");

        Pet pet = petService.getPetById(petId);

        // Get prescription history from repository
        List<PrescriptionHistory> histories = prescriptionHistoryRepository.findByPet(pet);

        // If no prescription histories exist, seed data
        if (histories.isEmpty()) {
            System.out.println("No prescription histories found. Seeding data...");
            seedPrescriptionHistories(petId);
            histories = prescriptionHistoryRepository.findByPet(pet); // Fetch again after seeding
        }

        return histories;
    }

    /**
     * Seeds default prescriptions into the database with userId and petId.
     */
    private void seedPrescriptions(Long petId) {
        System.out.println("Seeding prescription for pet with ID: " + petId);

        Pet pet = petService.getPetById(petId);

        // Set dummy start and end dates
        Date startDate1 = new Date();
        Date endDate1 = new Date();

        Date startDate2 = new Date();
        Date endDate2 = new Date();

        // Create prescription records with userId and petId
        Prescription prescription1 = new Prescription(
                pet,
                "Amoxicillin",
                "Dr. John Doe",
                "250mg twice a day",
                startDate1,
                endDate1,
                "For bacterial infection",
                12.3);

        Prescription prescription2 = new Prescription(
                pet,
                "Metronidazole",
                "Dr. Jane Smith",
                "500mg once a day",
                startDate2,
                endDate2,
                "For gastrointestinal issues",
                45.6);

        // Save the records to the database
        prescriptionRepository.save(prescription1);
        prescriptionRepository.save(prescription2);

        System.out.println("Prescriptions seeded.");
    }

    /**
     * Seeds default prescription history into the database with userId and petId.
     */
    private void seedPrescriptionHistories(Long petId) {
        System.out.println("Seeding prescription history for pet with ID: " + petId);

        Pet pet = petService.getPetById(petId);

        // Set dummy start and end dates
        Date startDate1 = new Date();
        Date endDate1 = new Date();

        Date startDate2 = new Date();
        Date endDate2 = new Date();

        // Create prescription history records with userId and petId
        PrescriptionHistory history1 = new PrescriptionHistory(
                pet,
                "Dr. Alice Green",
                "Amoxicillin",
                "Vet A",
                "250mg twice a day",
                startDate1,
                endDate1,
                "Follow up in a week");

        PrescriptionHistory history2 = new PrescriptionHistory(
                pet,
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
     * Adds a new prescription for a specific user and pet.
     * 
     * @param prescription The prescription to be added.
     * @return The added prescription.
     */
    @PostMapping("/add")
    @ResponseBody
    public Prescription addPrescription(@RequestBody Prescription prescription) {
        System.out.println("Adding prescription for petId: " + prescription.getPet().getId());
        return prescriptionRepository.save(prescription);
    }

    /**
     * Updates an existing prescription.
     * 
     * @param id                  The ID of the prescription to be updated.
     * @param updatedPrescription The updated prescription object.
     * @return The updated prescription.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id,
            @RequestBody Prescription updatedPrescription) {
        return prescriptionRepository.findById(id)
                .map(prescription -> {
                    // Update fields
                    prescription.setPet(updatedPrescription.getPet());
                    prescription.setPractitioner(updatedPrescription.getPractitioner());
                    prescription.setPrescription(updatedPrescription.getPrescription());
                    prescription.setVet(updatedPrescription.getVet());
                    prescription.setDosage(updatedPrescription.getDosage());
                    prescription.setStartDate(updatedPrescription.getStartDate());
                    prescription.setEndDate(updatedPrescription.getEndDate());
                    prescription.setDescription(updatedPrescription.getDescription());

                    Prescription savedPrescription = prescriptionRepository.save(prescription);
                    return ResponseEntity.ok(savedPrescription);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a prescription by its ID.
     * 
     * @param id The ID of the prescription to be deleted.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        if (prescriptionRepository.existsById(id)) {
            prescriptionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Fetches all pets associated with the logged-in vet.
     * This is used to display the pet selection screen.
     * 
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
        User user = userService.saveUser(new User("John", "Doe", "john.doe@example.com", "password123", "123456789"));
        
        // Seed pets
        Pet pet = new Pet(user, "Buddy", "Dog", "Golden Retriever", "Male", true, "Loves to play fetch", "2_buddy_retriever.png", LocalDate.of(2018, 1, 5));
        petService.save(pet);

        // Convert LocalDate to Date for medical history event dates
        Date historyDate1 = Date.from(LocalDate.of(2023, 1, 10).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Seed medical history
        MedicalHistory history = new MedicalHistory(pet, vet.getFullName(), "Routine checkup", vet, historyDate1, "All good", null);

        // Save the medical history records
        medicalHistoryService.save(history);
        
        seedPrescriptions(pet.getId());
        seedPrescriptionHistories(pet.getId());
    }

    /**
     * Adds a refill request for a specific prescription.
     *
     * @param prescriptionId The ID of the prescription to refill.
     * @param refillRequest  The refill request object from the client.
     * @return The created refill request.
     */
    @PostMapping("/refills/add")
    @ResponseBody
    public ResponseEntity<Refill> addRefill(@RequestBody Refill refillRequest) {
        // Validate refillRequest here (e.g., check if prescription exists)
        if (refillRequest.getPrescription() == null || refillRequest.getPrescription().getId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Generate a tracking number (e.g., alphanumeric with 12 characters)
        String trackingNumber = generateTrackingNumber(); // Implement this method to generate your tracking number
        refillRequest.setTracking(trackingNumber); // Set the generated tracking number

        // Assume you want to set some default fields or perform any operations here
        refillRequest.setFulfilled(false);
        Refill savedRefill = refillRepository.save(refillRequest);
        return ResponseEntity.ok(savedRefill);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        Optional<Prescription> prescription = prescriptionRepository.findById(id);
        if (prescription.isPresent()) {
            return ResponseEntity.ok(prescription.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Method to generate a tracking number
    private String generateTrackingNumber() {
        // Example of generating a random alphanumeric string of length 12
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder trackingNumber = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 12; i++) {
            int index = random.nextInt(characters.length());
            trackingNumber.append(characters.charAt(index));
        }

        return trackingNumber.toString();
    }

    @GetMapping("/refills/user/{userId}")
    public List<Refill> getRefillsByUserId(@PathVariable Long userId) {
        return refillRepository.findByUserId(userId);
    }

    /**
     * Deletes a refill by its ID.
     *
     * @param id The ID of the refill to be deleted.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @DeleteMapping("/refills/{id}")
    public ResponseEntity<Void> deleteRefill(@PathVariable Long id) {
        if (refillRepository.existsById(id)) {
            refillRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}
