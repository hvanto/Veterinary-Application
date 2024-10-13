package au.edu.rmit.sept.webapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;

/**
 * Represents a medical history record for a pet, including details about the treatment,
 * practitioner, veterinarian, event date, and associated prescription (if applicable).
 */
@Entity
@Table(name = "medical_history")
public class MedicalHistory {

    // Unique identifier for each medical history record
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The name of the practitioner who treated the pet (e.g., doctor or specialist)
    @Column(name = "practitioner", nullable = false)
    private String practitioner;

    // Description of the treatment administered to the pet
    @Column(name = "treatment", nullable = false)
    private String treatment;

    // Reference to the veterinarian who handled the case
    @ManyToOne
    @JoinColumn(name = "veterinarian_id", nullable = false)
    @JsonBackReference("veterinarian-medical-history")
    private Veterinarian veterinarian;

    // The date when the medical event or treatment occurred
    @Column(name = "event_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date eventDate;

    // Additional notes related to the medical event (optional)
    @Column(name = "notes")
    private String notes;

    // Reference to the pet to whom the medical history belongs
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    // Foreign key for prescription ID (automatically generated, not directly insertable/updatable)
    @Column(name = "prescription_id", insertable = false, updatable = false)
    private Long prescriptionId;

    // Reference to an optional prescription associated with the medical history
    @ManyToOne
    @JoinColumn(name = "prescription_id", nullable = true)
    private Prescription prescription;

    /**
     * Default constructor required by JPA.
     */
    public MedicalHistory() {
    }

    /**
     * Constructs a medical history object with all the necessary details.
     *
     * @param pet          The pet for whom this medical history record is created.
     * @param practitioner The practitioner who treated the pet.
     * @param treatment    Description of the treatment given to the pet.
     * @param veterinarian The veterinarian who handled the case.
     * @param eventDate    The date of the medical event.
     * @param notes        Additional notes about the event (optional).
     * @param prescription Prescription related to the event, if applicable.
     */
    public MedicalHistory(Pet pet, String practitioner, String treatment, Veterinarian veterinarian, Date eventDate, String notes, Prescription prescription) {
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

    /**
     * Returns the ID of the medical history record.
     *
     * @return the unique identifier of this record.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the medical history record.
     *
     * @param id the unique identifier to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the practitioner who treated the pet.
     *
     * @return the name of the practitioner.
     */
    public String getPractitioner() {
        return practitioner;
    }

    /**
     * Sets the name of the practitioner who treated the pet.
     *
     * @param practitioner the name of the practitioner.
     */
    public void setPractitioner(String practitioner) {
        this.practitioner = practitioner;
    }

    /**
     * Returns the treatment description for the medical history.
     *
     * @return the treatment description.
     */
    public String getTreatment() {
        return treatment;
    }

    /**
     * Returns the veterinarian associated with this medical history.
     *
     * @return the veterinarian handling the case.
     */
    public Veterinarian getVeterinarian() {
        return veterinarian;
    }

    /**
     * Sets the veterinarian associated with this medical history.
     *
     * @param veterinarian the veterinarian to set.
     */
    public void setVeterinarian(Veterinarian veterinarian) {
        this.veterinarian = veterinarian;
    }

    /**
     * Returns the event date of the medical history.
     *
     * @return the date of the medical event or treatment.
     */
    public Date getEventDate() {
        return eventDate;
    }

    /**
     * Returns additional notes related to the medical history (if any).
     *
     * @return notes regarding the medical event or treatment.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets additional notes related to the medical history.
     *
     * @param notes the notes to set for the medical history.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Returns the pet associated with the medical history.
     *
     * @return the pet that this medical history belongs to.
     */
    public Pet getPet() {
        return pet;
    }

    /**
     * Sets the pet associated with the medical history.
     *
     * @param pet the pet to set for the medical history.
     */
    public void setPet(Pet pet) {
        this.pet = pet;
    }

    /**
     * Returns the prescription ID associated with the medical history.
     * This is automatically generated and cannot be directly updated.
     *
     * @return the ID of the prescription.
     */
    public Long getPrescriptionId() {
        return prescriptionId;
    }

    /**
     * Returns the prescription associated with the medical history, if applicable.
     *
     * @return the prescription related to the medical event.
     */
    public Prescription getPrescription() {
        return prescription;
    }

    /**
     * Sets the prescription associated with the medical history.
     *
     * @param prescription the prescription to set.
     */
    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
        this.prescriptionId = prescription != null ? prescription.getId() : null;
    }
}