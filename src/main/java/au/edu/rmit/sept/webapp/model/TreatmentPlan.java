package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate planDate;

    @Column(length = 1000)
    private String description;

    private String practitioner;

    @Column(length = 1000)
    private String notes;

    private LocalDate followUpDate;

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "medical_history_id", nullable = true)
    private Long medicalHistoryId;

    // Getters
    public Long getId() { return id; }
    public LocalDate getPlanDate() { return planDate; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public String getPractitioner() { return practitioner; }
    public String getNotes() { return notes; }
    public LocalDate getFollowUpDate() { return followUpDate; }
    public Long getPetId() { return petId; }
    public Long getMedicalHistoryId() { return medicalHistoryId; }


    // Setters
    public void setPlanDate(LocalDate planDate) { this.planDate = planDate; }
    public void setDescription(String description) { this.description = description; }
    public void setPractitioner(String practitioner) { this.practitioner = practitioner;}
    public void setNotes(String notes) { this.notes = notes; }
    public void setFollowUpDate(LocalDate followUpDate) { this.followUpDate = followUpDate; }
    public void setPetId(Long petId) { this.petId = petId; }
    public void setMedicalHistoryId(Long medicalHistoryId) { this.medicalHistoryId = medicalHistoryId; }
}
