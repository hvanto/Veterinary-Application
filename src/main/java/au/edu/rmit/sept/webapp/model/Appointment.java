package au.edu.rmit.sept.webapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;

/**
 * @implNote 1. Pet column will be added to this Entity after the pet table has been created.
 */
@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    // Appointment date
    @Temporal(TemporalType.DATE)
    private Date appointmentDate;

    // Start and End times for the appointment
    @Temporal(TemporalType.TIME)
    private Date StartTime;

    @Temporal(TemporalType.TIME)
    private Date EndTime;

    private String Notes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-appointments")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "veterinarian_id", nullable = false)
    @JsonBackReference("veterinarian-appointments")
    private Veterinarian veterinarian;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pet_id", nullable = false)
    @JsonBackReference("pet-appointments")
    private Pet pet;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date CreatedOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date UpdatedOn;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean deleted;

    @PrePersist
    protected void onCreate() {
        CreatedOn = new Date();
        UpdatedOn = new Date();
        deleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedOn = new Date();
    }

    // Constructors
    public Appointment() {
    }

    public Appointment(Date appointmentDate, Date startTime, Date endTime, String notes, User user, Veterinarian veterinarian) {
        this.appointmentDate = appointmentDate;
        StartTime = startTime;
        EndTime = endTime;
        Notes = notes;
        this.user = user;
        this.veterinarian = veterinarian;
    }

    // Getters and Setters
    public Long getId() {
        return Id;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date startTime) {
        StartTime = startTime;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public void setEndTime(Date endTime) {
        EndTime = endTime;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Veterinarian getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(Veterinarian veterinarian) {
        this.veterinarian = veterinarian;
    }

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public Date getUpdatedOn() {
        return UpdatedOn;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}