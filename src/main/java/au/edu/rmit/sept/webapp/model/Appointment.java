package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * @implNote    1.Pet column will be added to this Entity after the pet table has been created.
 */
@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date DateTime;
    private String Notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    private Veterinarian veterinarian;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date CreatedOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date UpdatedOn;

    @PrePersist
    protected void onCreate() {
        CreatedOn = new Date();
        UpdatedOn = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedOn = new Date();
    }

    // Constructors
    public Appointment() {
    }

    public Appointment(Date dateTime, String notes, User user, Veterinarian veterinarian) {
        DateTime = dateTime;
        Notes = notes;
        this.user = user;
        this.veterinarian = veterinarian;
    }

    // Getters and Setters
    public Long getId() {
        return Id;
    }

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public Date getUpdatedOn() {
        return UpdatedOn;
    }

    public Date getDateTime() {
        return DateTime;
    }

    public void setDateTime(Date dateTime) {
        DateTime = dateTime;
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
}
