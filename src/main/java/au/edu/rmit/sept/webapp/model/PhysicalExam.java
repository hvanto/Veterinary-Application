package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "physical_exam")
public class PhysicalExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "exam_date", nullable = false)
    private Date examDate;

    @Column(name = "veterinarian", nullable = false)
    private String veterinarian;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    private MedicalHistory medicalHistory;

    // Default constructor
    public PhysicalExam(){}

    // Constructor for seeding data without medical history
    public PhysicalExam(Pet pet, LocalDate examDate, String veterinarian, String notes) {
        this.pet = pet;
        this.examDate = java.sql.Date.valueOf(examDate);
        this.veterinarian = veterinarian;
        this.notes = notes;
        this.medicalHistory = null; // or you can leave it uninitialized if that's acceptable
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Date getExamDate() {
        return examDate;
    }

    public String getVeterinarian() {
        return veterinarian;
    }

    public String getNotes() {
        return notes;
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

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public void setVeterinarian(String veterinarian) {
        this.veterinarian = veterinarian;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void setMedicalHistory(MedicalHistory medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
}
