package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Represents a MedicalHistory record for a pet.
 * This is the entity class that maps to the database table 'medical_history'.
 */
@Entity
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String practitioner;
    private String treatment;
    private String veterinarian;

    @Temporal(TemporalType.DATE)
    private Date eventDate;

    private String notes;

    private Long petId;
    private Long prescriptionId;

    // Getters
    public Long getId() {
        return id;
    }
    public String getPractitioner() {
        return practitioner;
    }
    public String getTreatment() {
        return treatment;
    }
    public String getVeterinarian() {
        return veterinarian;
    }
    public Date getEventDate() {
        return eventDate;
    }
    public String getNotes() {
        return notes;
    }
    public Long getPetId() {
        return petId;
    }
    public Long getPrescriptionId() {
        return prescriptionId;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setPractitioner(String practitioner) {
        this.practitioner = practitioner;
    }
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
    public void setVeterinarian(String veterinarian) {
        this.veterinarian = veterinarian;
    }
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setPetId(Long petId) {
        this.petId = petId;
    }
    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }
}
