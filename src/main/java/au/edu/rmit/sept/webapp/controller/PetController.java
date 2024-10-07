package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.controller.PetController.PetRequest;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.PetRepository;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

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

    




    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Pet>> getPetsByUserId(@PathVariable Long userId) {
        List<Pet> pets = petService.getPetsByUserId(userId);
        if (pets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pets); 
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePet(@PathVariable Long id, @RequestBody Pet updatedPet) {
        Pet existingPet = petService.getPetById(id);
        if (existingPet == null) {
            return ResponseEntity.badRequest().body("Pet not found");
        }
    
        // Update pet details
        existingPet.setName(updatedPet.getName());
        existingPet.setSpecies(updatedPet.getSpecies());
        existingPet.setBreed(updatedPet.getBreed());
        existingPet.setGender(updatedPet.getGender());
        existingPet.setMicrochipped(updatedPet.isMicrochipped());
        existingPet.setDateOfBirth(updatedPet.getDateOfBirth());
        existingPet.setNotes(updatedPet.getNotes());
    
        // Save the updated pet
        petService.save(existingPet);
        return ResponseEntity.ok("Pet updated successfully!");
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
