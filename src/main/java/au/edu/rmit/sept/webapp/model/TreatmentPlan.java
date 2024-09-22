package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

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

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    private MedicalHistory medicalHistory;

    // Default constructor
    public TreatmentPlan() {}

    // Constructor for seeding data
    public TreatmentPlan(Pet pet, LocalDate planDate, String description, String practitioner, String notes, LocalDate followUpDate) {
        this.pet = pet;
        this.planDate = planDate;
        this.description = description;
        this.practitioner = practitioner;
        this.notes = notes;
        this.followUpDate = followUpDate;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public LocalDate getPlanDate() {
        return planDate;
    }

    public String getDescription() {
        return description;
    }

    public String getPractitioner() {
        return practitioner;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDate getFollowUpDate() {
        return followUpDate;
    }

    public Pet getPet() {
        return pet;
    }

    public MedicalHistory getMedicalHistory() {
        return medicalHistory;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setPlanDate(LocalDate planDate) {
        this.planDate = planDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPractitioner(String practitioner) {
        this.practitioner = practitioner;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setFollowUpDate(LocalDate followUpDate) {
        this.followUpDate = followUpDate;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void setMedicalHistory(MedicalHistory medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
}
