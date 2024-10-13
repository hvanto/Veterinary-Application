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
    private String email;
    private String Contact;
    private String Image;

    private String Password;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    @JsonBackReference("clinic-veterinarians")
    private Clinic clinic;

    @ManyToMany
    @JoinTable(
            name = "veterinarian_service",
            joinColumns = @JoinColumn(name = "veterinarian_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @Fetch(FetchMode.JOIN)
//    @JsonManagedReference("veterinarian-services")
    private List<Service> services;

    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JsonManagedReference("veterinarian-appointments")
    private List<Appointment> appointments;

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
        this.email = email;
        Contact = contact;
        Password = password;
    }

    // Getters and Setters
    public Long getId() {
        return Id;
    }

    public void setId(Long Id) { this.Id = Id; }

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

    public String getFullName() {
        return this.FirstName + " " + this.LastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
