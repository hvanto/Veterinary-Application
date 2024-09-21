package au.edu.rmit.sept.webapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String Title;
    private String Description;

    @ManyToMany(mappedBy = "services")
    @Fetch(FetchMode.JOIN)
    @JsonBackReference // Prevent infinite recursion with Veterinarians
    private List<Veterinarian> veterinarians;

    // Constructors
    public Service() {}

    public Service(String title, String description) {
        this.Title = title;
        this.Description = description;
    }

    // Getters and Setters
    public Long getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<Veterinarian> getVeterinarians() {
        return veterinarians;
    }

    public void setVeterinarians(List<Veterinarian> veterinarians) {
        this.veterinarians = veterinarians;
    }
}
