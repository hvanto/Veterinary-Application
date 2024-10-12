package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

import java.util.Date;

/**
 * Represents a vaccination record for a pet.
 * It tracks details of the vaccine administered, the veterinarian who administered it,
 * and the next scheduled vaccination date.
 */
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

    /**
     * Default constructor for JPA.
     */
    public Vaccination() {
    }

    /**
     * Constructor for creating a vaccination record with all fields.
     *
     * @param pet             the pet receiving the vaccination
     * @param vaccineName     the name of the vaccine
     * @param vaccinationDate the date the vaccination was administered
     * @param administeredBy  the name of the veterinarian or practitioner administering the vaccine
     * @param nextDueDate     the next scheduled date for the vaccine
     */
    public Vaccination(Pet pet, String vaccineName, Date vaccinationDate, String administeredBy, Date nextDueDate) {
        this.pet = pet;
        this.vaccineName = vaccineName;
        this.vaccinationDate = vaccinationDate;
        this.administeredBy = administeredBy;
        this.nextDueDate = nextDueDate;
    }

    // Getters and Setters

    /**
     * @return the unique identifier of the vaccination record
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this vaccination record.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name of the vaccine administered
     */
    public String getVaccineName() {
        return vaccineName;
    }

    /**
     * @return the date the vaccination was administered
     */
    public Date getVaccinationDate() {
        return vaccinationDate;
    }

    /**
     * @return the name of the practitioner who administered the vaccination
     */
    public String getAdministeredBy() {
        return administeredBy;
    }

    /**
     * @return the next scheduled vaccination date, if applicable
     */
    public Date getNextDueDate() {
        return nextDueDate;
    }

    /**
     * @return the pet that received the vaccination
     */
    public Pet getPet() {
        return pet;
    }

    /**
     * Sets the pet that received the vaccination.
     *
     * @param pet the pet to associate with the vaccination
     */
    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
