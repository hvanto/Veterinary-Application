package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;
    private final UserService userService;

    @Autowired
    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    // Add a new pet for the logged-in user
    @PostMapping("/add")
    public ResponseEntity<String> addPet(@RequestBody PetRequest petRequest) {
        Optional<User> userOptional = userService.findById(petRequest.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();
        Pet newPet = new Pet(user, petRequest.getName(), petRequest.getSpecies(), petRequest.getBreed(),
                             petRequest.getGender(), petRequest.isMicrochipped(), petRequest.getNotes(),
                             null, LocalDate.parse(petRequest.getDateOfBirth()));

        petService.save(newPet);
        return ResponseEntity.ok("Pet added successfully!");
    }

    // PetRequest DTO class defined inside the PetController
    public static class PetRequest {
        private String name;
        private String gender;
        private String species;
        private String breed;
        private boolean microchipped;
        private String notes;
        private String dateOfBirth;
        private Long userId;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getSpecies() {
            return species;
        }

        public void setSpecies(String species) {
            this.species = species;
        }

        public String getBreed() {
            return breed;
        }

        public void setBreed(String breed) {
            this.breed = breed;
        }

        public boolean isMicrochipped() {
            return microchipped;
        }

        public void setMicrochipped(boolean microchipped) {
            this.microchipped = microchipped;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }
}
