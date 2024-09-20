package au.edu.rmit.sept.webapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "veterinarian")
public class Veterinarian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String FirstName;
    private String LastName;

    @Column(unique = true)
    private String Email;
    private String Contact;
    private String Image;

    private String Password;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    @JsonBackReference // Prevent infinite loop with Clinic
    private Clinic clinic;

    @ManyToMany
    @JoinTable(
            name = "veterinarian_service",
            joinColumns = @JoinColumn(name = "veterinarian_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @Fetch(FetchMode.JOIN)
    @JsonManagedReference
    private List<Service> services;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date CreatedOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date UpdatedOn;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean Deleted;

    @PrePersist
    protected void onCreate() {
        Contact = "0000000000";
        Image = "default_profile.png";
        CreatedOn = new Date();
        UpdatedOn = new Date();
        Deleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedOn = new Date();
    }

    // Constructors
    public Veterinarian() {}

    public Veterinarian(String firstName, String lastName, String email, String contact, String password) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        Contact = contact;
        Password = password;
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

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }
}
