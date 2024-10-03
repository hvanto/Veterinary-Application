package au.edu.rmit.sept.webapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "species")
    private String species;

    @Column(name = "breed")
    private String breed;

    @Column(name = "microchipped", nullable = false)
    private boolean microchipped;

    @Column(name = "notes")
    private String notes;

    @Column(name = "image_path")
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-pets") 
    private User user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    // Default constructor
    public Pet() {
    }

    // Constructor for seeding data
    public Pet(User user, String name, String species, String breed, String gender, boolean microchipped, String notes, String imagePath, LocalDate dateOfBirth) {
        this.user = user;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.gender = gender;
        this.microchipped = microchipped;
        this.notes = notes;
        this.imagePath = imagePath;
        this.dateOfBirth = java.sql.Date.valueOf(dateOfBirth);
    }

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

    public boolean isMicrochipped() {
        return microchipped;
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
}
