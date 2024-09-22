package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

/**
 * Represents a weight record for a pet.
 * Each record is associated with a specific pet and stores the weight on a specific date.
 */
@Entity
@Table(name = "weight_record")
public class WeightRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "record_date", nullable = false)
    private Date recordDate;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "medical_history_id", nullable = true)
    private MedicalHistory medicalHistory;

    // Default constructor
    public WeightRecord(){};

    // Constructor for creating instances
    public WeightRecord(Pet pet, Date recordDate, Double weight) {
        this.pet = pet;
        this.recordDate = recordDate;
        this.weight = weight;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public MedicalHistory getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(MedicalHistory medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
}
