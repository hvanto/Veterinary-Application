package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

import java.util.Date;

/**
 * Represents a weight record for a pet.
 * Each record stores the pet's weight on a specific date and is associated with a particular pet.
 * It may also be linked to a medical history record if relevant.
 */
@Entity
@Table(name = "weight_record")
public class WeightRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The date when the weight was recorded.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "record_date", nullable = false)
    private Date recordDate;

    /**
     * The pet's weight on the recorded date.
     */
    @Column(name = "weight", nullable = false)
    private Double weight;

    /**
     * The pet for which this weight record is created.
     */
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    /**
     * The associated medical history for this weight record, if applicable.
     */
    @ManyToOne
    @JoinColumn(name = "medical_history_id", nullable = true)
    private MedicalHistory medicalHistory;

    /**
     * Default constructor for JPA.
     */
    public WeightRecord() {
    }

    /**
     * Constructor for creating a weight record instance.
     *
     * @param pet        The pet for which the weight is recorded.
     * @param recordDate The date when the weight was recorded.
     * @param weight     The pet's weight on the recorded date.
     */
    public WeightRecord(Pet pet, Date recordDate, Double weight) {
        this.pet = pet;
        this.recordDate = recordDate;
        this.weight = weight;
    }

    // Getters and Setters

    /**
     * @return the unique identifier of the weight record.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the weight record.
     *
     * @param id the ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the date the weight was recorded.
     */
    public Date getRecordDate() {
        return recordDate;
    }

    /**
     * sets the date of the record
     */
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    /**
     * @return the weight of the pet on the recorded date.
     */
    public Double getWeight() {
        return weight;
    }

    /**
     * sets the weight of the pet.
     */
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * @return the pet associated with this weight record.
     */
    public Pet getPet() {
        return pet;
    }

    /**
     * Sets the pet for this weight record.
     *
     * @param pet the pet to set.
     */
    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
