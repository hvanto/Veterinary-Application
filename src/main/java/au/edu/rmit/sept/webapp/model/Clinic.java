package au.edu.rmit.sept.webapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Table(name = "clinic")
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToMany(mappedBy = "clinic")
    @Fetch(FetchMode.JOIN)
    @JsonManagedReference // Break the infinite loop
    private List<Veterinarian> veterinarians;

    private String Name;
    private String Email;
    private String Address;
    private String location;
    private Integer OpeningTime;
    private Integer ClosingTime;
    private Long Contact;

    // Constructors
    public Clinic() {}

    public Clinic(String name, String email, String address, String location, Integer openingTime, Integer closingTime, Long contact) {
        Name = name;
        Email = email;
        Address = address;
        this.location = location;
        OpeningTime = openingTime;
        ClosingTime = closingTime;
        Contact = contact;
    }

    // Getters and Setters

    public Long getId() {
        return Id;
    }

    public List<Veterinarian> getVeterinarians() {
        return veterinarians;
    }

    public void setVeterinarians(List<Veterinarian> veterinarians) {
        veterinarians = veterinarians;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getOpeningTime() {
        return OpeningTime;
    }

    public void setOpeningTime(Integer openingTime) {
        OpeningTime = openingTime;
    }

    public Integer getClosingTime() {
        return ClosingTime;
    }

    public void setClosingTime(Integer closingTime) {
        ClosingTime = closingTime;
    }

    public Long getContact() {
        return Contact;
    }

    public void setContact(Long contact) {
        Contact = contact;
    }
}
