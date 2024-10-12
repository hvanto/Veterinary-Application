package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "prescription_history")
public class PrescriptionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(name = "practitioner", nullable = false)
    private String practitioner;

    @Column(name = "prescription", nullable = false)
    private String prescription;

    @Column(name = "vet", nullable = true)
    private String vet;

    @Column(name = "dosage", nullable = false)
    private String dosage;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "more_information", nullable = true)
    private String moreInformation;

    // Constructors
    public PrescriptionHistory() {}

    public PrescriptionHistory(Pet pet, String practitioner, String prescription, String vet, String dosage, Date startDate, Date endDate, String moreInformation) {
        this.pet = pet;
        this.practitioner = practitioner;
        this.prescription = prescription;
        this.vet = vet;
        this.dosage = dosage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.moreInformation = moreInformation;
    }

    public PrescriptionHistory(Pet pet, String practitioner, String prescription, String dosage, Date startDate, Date endDate, String moreInformation) {
        this.pet = pet;
        this.practitioner = practitioner;
        this.prescription = prescription;
        this.dosage = dosage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.moreInformation = moreInformation;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public String getPractitioner() {
        return practitioner;
    }

    public void setPractitioner(String practitioner) {
        this.practitioner = practitioner;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getVet() {
        return vet;
    }

    public void setVet(String vet) {
        this.vet = vet;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getMoreInformation() {
        return moreInformation;
    }

    public void setMoreInformation(String moreInformation) {
        this.moreInformation = moreInformation;
    }
}
