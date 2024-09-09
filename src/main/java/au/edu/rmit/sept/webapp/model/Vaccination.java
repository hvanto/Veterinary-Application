package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vaccine_name", nullable = false)
    private String vaccineName;

    @Column(name = "vaccination_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date vaccinationDate;

    @Column(name = "administered_by", nullable = false)
    private String administeredBy;

    @Column(name = "next_due_date")
    @Temporal(TemporalType.DATE)
    private Date nextDueDate;

    @ManyToOne
    @JoinColumn(name = "medical_history_id", nullable = true)
    private MedicalHistory medicalHistory;

    // Getters
    public Long getId() {
        return id;
    }
    public String getVaccineName() {
        return vaccineName;
    }
    public Date getVaccinationDate() {
        return vaccinationDate;
    }
    public String getAdministeredBy() {
        return administeredBy;
    }
    public Date getNextDueDate() {
        return nextDueDate;
    }
    public MedicalHistory getMedicalHistory() {
        return medicalHistory;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }
    public void setVaccinationDate(Date vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }
    public void setAdministeredBy(String administeredBy) {
        this.administeredBy = administeredBy;
    }
    public void setNextDueDate(Date nextDueDate) {
        this.nextDueDate = nextDueDate;
    }
    public void setMedicalHistory(MedicalHistory medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
}
