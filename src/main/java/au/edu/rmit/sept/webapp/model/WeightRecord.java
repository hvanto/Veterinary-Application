package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Represents a weight record for a pet.
 * Each record is associated with a specific pet and stores the weight on a specific date.
 */
@Entity
public class WeightRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private Double weight;

    @Temporal(TemporalType.DATE)
    private Date recordDate;

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    public Long getId() {
        return id;
    }
    public Date getRecordDate() {
        return recordDate;
    }
    public Double getWeight() {
        return weight;
    }
    public Long getPetId() {
        return petId;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    public void setPetId(Long petId) {
        this.petId = petId;
    }
}
