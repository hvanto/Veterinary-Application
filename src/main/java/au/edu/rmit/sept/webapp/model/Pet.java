package au.edu.rmit.sept.webapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String gender;
    private String species;
    private String breed;
    private boolean microchipped;
    private String notes;

    @Column(name = "image_path") // Map to "image_path" column in the database
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference // Prevent infinite loop with Clinic
    private User user;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    // Getters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getGender() {
        return gender;
    }
    public String getSpecies() {
        return species;
    }
    public String getBreed() {
        return breed;
    }
    public String getNotes() {
        return notes;
    }
    public String getImagePath() {
        return imagePath;
    }
    public User getUser() {
        return user;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        long diff = new Date().getTime() - dateOfBirth.getTime();
        return (int) (diff / (1000L * 60 * 60 * 24 * 365));
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setSpecies(String species) {
        this.species = species;
    }
    public void setBreed(String breed) {
        this.breed = breed;
    }
    public void setMicrochipped(boolean microchipped) {
        this.microchipped = microchipped;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Other Methods
    public boolean isMicrochipped() {
        return microchipped;
    }
}
