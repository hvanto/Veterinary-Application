package au.edu.rmit.sept.webapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * The Pet class represents a pet entity within the system.
 * It stores basic pet information such as name, species, breed, and more.
 * It also maintains relationships with its owner (User) and related appointments.
 */
@Entity
@Table(name = "pet")
public class Pet {

    // Unique identifier for each pet
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pet's name
    @Column(name = "name", nullable = false)
    private String name;

    // Pet's gender
    @Column(name = "gender")
    private String gender;

    // Pet's species (e.g., dog, cat)
    @Column(name = "species")
    private String species;

    // Pet's breed (e.g., Golden Retriever, Siamese)
    @Column(name = "breed")
    private String breed;

    // Indicates if the pet is microchipped
    @Column(name = "microchipped", nullable = false)
    private boolean microchipped;

    // Additional notes regarding the pet (optional)
    @Column(name = "notes")
    private String notes;

    // Path to the image of the pet
    @Column(name = "image_path")
    private String imagePath;

    // Many-to-One relationship with the user (owner of the pet)
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-pets") // Prevents circular reference during JSON serialization
    private User user;

    // Pet's date of birth
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    // One-to-Many relationship with appointments related to the pet
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JsonManagedReference("pet-appointments") // Manages forward references during JSON serialization
    private List<Appointment> appointments;

    /**
     * Default constructor required by JPA.
     */
    public Pet() {
    }

    /**
     * Constructs a Pet object with the necessary details for seeding data.
     *
     * @param user         The user who owns the pet.
     * @param name         The name of the pet.
     * @param species      The species of the pet (e.g., dog, cat).
     * @param breed        The breed of the pet.
     * @param gender       The gender of the pet.
     * @param microchipped Indicates if the pet is microchipped.
     * @param notes        Any additional notes regarding the pet.
     * @param imagePath    The image path for the pet.
     * @param dateOfBirth  The date of birth of the pet.
     */
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

    public User getUser() {
        return user;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
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

    // Getter and Setter for imagePath
    public String getImagePath() {
        return imagePath;
    }
}
