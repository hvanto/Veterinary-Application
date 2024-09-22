package au.edu.rmit.sept.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public List<Prescription> getCurrentPrescriptions(Pet pet) {
        return prescriptionRepository.findCurrentPrescriptionsByPet(pet);
    }

    public List<Prescription> getPrescriptionHistory(Pet pet) {
        return prescriptionRepository.findPrescriptionHistoryByPet(pet);
    }

    public Prescription addPrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    public Prescription updatePrescription(Long id, Prescription updatedPrescription) {
        return prescriptionRepository.findById(id)
            .map(existingPrescription -> {
                existingPrescription.setMedication(updatedPrescription.getMedication());
                existingPrescription.setDosage(updatedPrescription.getDosage());
                existingPrescription.setFrequency(updatedPrescription.getFrequency());
                existingPrescription.setDuration(updatedPrescription.getDuration());
                existingPrescription.setIssueDate(updatedPrescription.getIssueDate());
                existingPrescription.setRefillDate(updatedPrescription.getRefillDate());
                return prescriptionRepository.save(existingPrescription);
            }).orElseThrow(() -> new IllegalArgumentException("Prescription with id " + id + " not found"));
    }

    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Prescription with id " + id + " not found"));
        prescriptionRepository.delete(prescription);
    }
    
}
