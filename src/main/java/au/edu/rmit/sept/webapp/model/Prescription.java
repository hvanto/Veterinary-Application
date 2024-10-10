package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "description", nullable = false)
    private String description;

    // Constructors
    public Prescription() {}

    public Prescription(User user, Pet pet, String prescription, String practitioner, String dosage, Date startDate, Date endDate, String description) {
        this.user = user;
        this.pet = pet;
        this.prescription = prescription;
        this.practitioner = practitioner;
        this.dosage = dosage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public Prescription(User user, Pet pet, String prescription, String practitioner, String vet, String dosage, Date startDate, Date endDate, String description) {
        this.user = user;
        this.pet = pet;
        this.prescription = prescription;
        this.practitioner = practitioner;
        this.vet = vet;
        this.dosage = dosage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getPractitioner() {
        return practitioner;
    }

    public void setPractitioner(String practitioner) {
        this.practitioner = practitioner;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
