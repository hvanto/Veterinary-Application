package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.model.PrescriptionHistory;
import au.edu.rmit.sept.webapp.repository.PrescriptionRepository;
import au.edu.rmit.sept.webapp.service.MedicalHistoryService;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.repository.PrescriptionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PetService petService;

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

    @GetMapping("/vet-pets")
    @ResponseBody
    public List<Pet> getUserPets(@RequestParam Long vetId) {
        System.out.println("Received request for pets with vetId: " + vetId);
        List<Pet> pets = petService.getPetsByUserId(vetId);
        return pets;
    }
}
