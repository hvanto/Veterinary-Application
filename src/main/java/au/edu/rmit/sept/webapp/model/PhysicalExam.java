package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

/**
 * The PhysicalExam class represents a physical examination of a pet.
 * Each physical exam includes details such as the date of the exam,
 * the veterinarian performing the exam, and any relevant notes.
 * This is linked to a pet and may be optionally linked to a medical history record.
 */
@Entity
@Table(name = "physical_exam")
public class PhysicalExam {

    /**
     * The unique identifier for the physical exam.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The date the physical exam took place.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "exam_date", nullable = false)
    private Date examDate;

    /**
     * The name of the veterinarian who conducted the physical exam.
     */
    @Column(name = "veterinarian", nullable = false)
    private String veterinarian;

    /**
     * Any additional notes or observations recorded during the physical exam.
     */
    @Column(name = "notes")
    private String notes;

    /**
     * The pet that the physical exam is associated with.
     */
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    /**
     * The optional medical history entry related to this physical exam, if any.
     */
    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    private MedicalHistory medicalHistory;

    /**
     * Default constructor for the PhysicalExam class.
     */
    public PhysicalExam() {
    }

    /**
     * Constructor to create a PhysicalExam without linking to medical history.
     *
     * @param pet          The pet that the exam is associated with.
     * @param examDate     The date the physical exam took place.
     * @param veterinarian The veterinarian who conducted the exam.
     * @param notes        Any additional notes or observations from the exam.
     */
    public PhysicalExam(Pet pet, LocalDate examDate, String veterinarian, String notes) {
        this.pet = pet;
        this.examDate = java.sql.Date.valueOf(examDate);
        this.veterinarian = veterinarian;
        this.notes = notes;
        this.medicalHistory = null; // Initialize to null if no medical history is linked
    }

    // Getters

    /**
     * Gets the unique ID of the physical exam.
     *
     * @return the ID of the physical exam.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the date of the physical exam.
     *
     * @return the date of the physical exam.
     */
    public Date getExamDate() {
        return examDate;
    }

    /**
     * Gets the name of the veterinarian who conducted the exam.
     *
     * @return the name of the veterinarian.
     */
    public String getVeterinarian() {
        return veterinarian;
    }

    /**
     * Gets any notes or observations from the physical exam.
     *
     * @return notes from the physical exam.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Gets the pet that this physical exam is associated with.
     *
     * @return the pet associated with this exam.
     */
    public Pet getPet() {
        return pet;
    }

    // Setters

    /**
     * Sets the unique ID of the physical exam.
     *
     * @param id the ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the name of the veterinarian who conducted the exam.
     *
     * @param veterinarian the name of the veterinarian.
     */
    public void setVeterinarian(String veterinarian) {
        this.veterinarian = veterinarian;
    }

    /**
     * Sets any notes or observations from the physical exam.
     *
     * @param notes the notes to set.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Sets the pet that this physical exam is associated with.
     *
     * @param pet the pet to associate with this exam.
     */
    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
