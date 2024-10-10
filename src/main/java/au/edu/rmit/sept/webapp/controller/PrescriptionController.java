package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.model.PrescriptionHistory;
import au.edu.rmit.sept.webapp.repository.PrescriptionRepository;
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
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionHistoryRepository prescriptionHistoryRepository;

    /**
     * Fetches all prescriptions for a specific user and pet.
     * @param userId ID of the user.
     * @param petId ID of the pet.
     * @return List of prescriptions.
     */
    @GetMapping("/all")
    @ResponseBody
    public List<Prescription> getAllPrescriptions(@RequestParam Long userId, @RequestParam Long petId) {
        System.out.println("Fetching all prescriptions for userId: " + userId + " and petId: " + petId);

        return prescriptionRepository.findByUserIdAndPetId(userId, petId);
    }

    /**
     * Fetches all prescription history records for a specific user and pet.
     * @param userId ID of the user.
     * @param petId ID of the pet.
     * @return List of prescription histories.
     */
    @GetMapping("/history/all")
    @ResponseBody
    public List<PrescriptionHistory> getAllPrescriptionHistories(@RequestParam Long userId, @RequestParam Long petId) {
        System.out.println("Fetching all prescription histories for userId: " + userId + " and petId: " + petId);

        return prescriptionHistoryRepository.findByUserIdAndPetId(userId, petId);
    }

    /**
     * Seeds default prescriptions into the database with userId and petId.
     */
    private void seedPrescriptions() {
        // Set dummy start and end dates
        Date startDate1 = new Date();
        Date endDate1 = new Date();

        Date startDate2 = new Date();
        Date endDate2 = new Date();

        // Create prescription records with userId and petId
        Prescription prescription1 = new Prescription(
                1L, 17L, "Amoxicillin",
                "Dr. John Doe",
                "250mg twice a day",
                startDate1, endDate1,
                "For bacterial infection"
        );

        Prescription prescription2 = new Prescription(
                1L, 18L, "Metronidazole",
                "Dr. Jane Smith",
                "500mg once a day",
                startDate2, endDate2,
                "For gastrointestinal issues"
        );

        // Save the records to the database
        prescriptionRepository.save(prescription1);
        prescriptionRepository.save(prescription2);

        System.out.println("Prescriptions seeded.");
    }

    /**
     * Seeds default prescription history into the database with userId and petId.
     */
    private void seedPrescriptionHistories() {
        // Set dummy start and end dates
        Date startDate1 = new Date();
        Date endDate1 = new Date();

        Date startDate2 = new Date();
        Date endDate2 = new Date();

        // Create prescription history records with userId and petId
        PrescriptionHistory history1 = new PrescriptionHistory(
                1L, 17L, "Dr. Alice Green",
                "Amoxicillin",
                "Vet A",
                "250mg twice a day",
                startDate1, endDate1,
                "Follow up in a week"
        );

        PrescriptionHistory history2 = new PrescriptionHistory(
                1L, 18L, "Dr. Bob White",
                "Metronidazole",
                "Vet B",
                "500mg once a day",
                startDate2, endDate2,
                "Monitor for side effects"
        );

        // Save the records to the database
        prescriptionHistoryRepository.save(history1);
        prescriptionHistoryRepository.save(history2);

        System.out.println("Prescription histories seeded.");
    }

    /**
     * Adds a new prescription for a specific user and pet.
     * @param prescription The prescription to be added.
     * @return The added prescription.
     */
    @PostMapping("/add")
    @ResponseBody
    public Prescription addPrescription(@RequestBody Prescription prescription) {
        System.out.println("Adding prescription for userId: " + prescription.getUserId() + " and petId: " + prescription.getPetId());
        return prescriptionRepository.save(prescription);
    }

    /**
     * Updates an existing prescription.
     * @param id The ID of the prescription to be updated.
     * @param updatedPrescription The updated prescription object.
     * @return The updated prescription.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id, @RequestBody Prescription updatedPrescription) {
        return prescriptionRepository.findById(id)
                .map(prescription -> {
                    // Update fields
                    prescription.setUserId(updatedPrescription.getUserId());
                    prescription.setPetId(updatedPrescription.getPetId());
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
}
