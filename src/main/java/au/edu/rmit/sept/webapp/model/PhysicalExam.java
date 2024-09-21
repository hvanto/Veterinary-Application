package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;
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
}
