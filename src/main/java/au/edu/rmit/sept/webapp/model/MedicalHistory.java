package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "medical_history")
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "practitioner", nullable = false)
    private String practitioner;

    @Column(name = "treatment", nullable = false)
    private String treatment;

    @Column(name = "veterinarian", nullable = false)
    private String veterinarian;

    @Column(name = "event_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date eventDate;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(name = "prescription_id", insertable = false, updatable = false)
    private Long prescriptionId;

    @ManyToOne
    @JoinColumn(name = "prescription_id", nullable = true)
    private Prescription prescription;

    // Default constructor
    public MedicalHistory() {
    }

    // Constructor including all fields
    public MedicalHistory(Pet pet, String practitioner, String treatment, String veterinarian, Date eventDate, String notes, Prescription prescription) {
        this.pet = pet;
        this.practitioner = practitioner;
        this.treatment = treatment;
        this.veterinarian = veterinarian;
        this.eventDate = eventDate;
        this.notes = notes;
        this.prescription = prescription;
        this.prescriptionId = prescription != null ? prescription.getId() : null;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPractitioner() {
        return practitioner;
    }

    public void setPractitioner(String practitioner) {
        this.practitioner = practitioner;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(String veterinarian) {
        this.veterinarian = veterinarian;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
        this.prescriptionId = prescription != null ? prescription.getId() : null;
    }
}
