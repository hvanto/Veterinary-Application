package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Represents a treatment plan for a pet, detailing specific treatments
 * and procedures recommended by a practitioner.
 */
@Entity
@Table(name = "treatment_plan")
public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_date")
    private LocalDate planDate;

    @Column(length = 1000)
    private String description;

    private String practitioner;

    @Column(length = 1000)
    private String notes;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    private MedicalHistory medicalHistory;

    /**
     * Default constructor for JPA.
     */
    public TreatmentPlan() {
    }

    /**
     * Constructor used to create a TreatmentPlan with all the necessary fields.
     *
     * @param pet          the pet associated with the treatment plan
     * @param planDate     the date the treatment plan was created
     * @param description  description of the treatment
     * @param practitioner name of the practitioner providing the plan
     * @param notes        any additional notes for the treatment
     */
    public TreatmentPlan(Pet pet, LocalDate planDate, String description, String practitioner, String notes) {
        this.pet = pet;
        this.planDate = planDate;
        this.description = description;
        this.practitioner = practitioner;
        this.notes = notes;
    }

    // Getters

    /**
     * @return the unique identifier for this treatment plan
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the date this treatment plan was created
     */
    public LocalDate getPlanDate() {
        return planDate;
    }

    /**
     * @return the description of the treatment plan
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the name of the practitioner who created the treatment plan
     */
    public String getPractitioner() {
        return practitioner;
    }

    /**
     * @return any additional notes about the treatment
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @return the pet associated with this treatment plan
     */
    public Pet getPet() {
        return pet;
    }

    // Setters

    /**
     * Sets the unique identifier for this treatment plan.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the description for the treatment plan.
     *
     * @param description a detailed description of the treatment
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the practitioner responsible for the treatment plan.
     *
     * @param practitioner the practitioner's name
     */
    public void setPractitioner(String practitioner) {
        this.practitioner = practitioner;
    }

    /**
     * Sets additional notes for the treatment plan.
     *
     * @param notes additional notes or instructions for the treatment
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Associates this treatment plan with a specific pet.
     *
     * @param pet the pet receiving the treatment
     */
    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
