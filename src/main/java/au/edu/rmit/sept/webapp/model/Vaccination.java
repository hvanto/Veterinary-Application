package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "vaccination")
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vaccine_name", nullable = false)
    private String vaccineName;

    @Temporal(TemporalType.DATE)
    @Column(name = "vaccination_date", nullable = false)
    private Date vaccinationDate;

    @Column(name = "administered_by", nullable = false)
    private String administeredBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "next_due_date")
    private Date nextDueDate;

    @ManyToOne
    @JoinColumn(name = "medical_history_id", nullable = true)
    private MedicalHistory medicalHistory;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    // Default constructor
    public Vaccination() {}

    // Constructor for creating instances
    public Vaccination(Pet pet, String vaccineName, Date vaccinationDate, String administeredBy, Date nextDueDate) {
        this.pet = pet;
        this.vaccineName = vaccineName;
        this.vaccinationDate = vaccinationDate;
        this.administeredBy = administeredBy;
        this.nextDueDate = nextDueDate;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public Date getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(Date vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    public String getAdministeredBy() {
        return administeredBy;
    }

    public void setAdministeredBy(String administeredBy) {
        this.administeredBy = administeredBy;
    }

    public Date getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(Date nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public MedicalHistory getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(MedicalHistory medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
