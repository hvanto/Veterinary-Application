package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.controller.PetController.PetRequest;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.PetRepository;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import java.nio.file.Path;

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

    @PostMapping("/add")
    public ResponseEntity<String> addPet(
            @RequestParam("name") String name,
            @RequestParam("gender") String gender,
            @RequestParam("species") String species,
            @RequestParam("breed") String breed,
            @RequestParam("microchipped") boolean microchipped,
            @RequestParam("dateOfBirth") String dateOfBirth,
            @RequestParam("notes") String notes,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();
        Pet newPet = new Pet(user, name, species, breed, gender, microchipped, notes, null, LocalDate.parse(dateOfBirth));

        // Handle image upload
        if (image != null && !image.isEmpty()) {
            try {
                String imagePath = saveImage(image);
                newPet.setImagePath(imagePath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save image.");
            }
        }

        petService.save(newPet);
        return ResponseEntity.ok("Pet added successfully!");
    }


    private String saveImage(MultipartFile image) throws IOException {
        // Set the absolute path to "assets" directory under "static"
        String uploadDir = Paths.get("src/main/resources/static/assets").toAbsolutePath().toString();
        
        // Ensure the directory exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    
        // Generate a unique filename
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        
        // Save the file to the specified path
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    
        // Return the relative path that can be stored in the database
        return "/assets/" + fileName;
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

    // Delete pet by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePet(@PathVariable Long id) {
        Pet pet = petService.getPetById(id);
        if (pet == null) {
            return ResponseEntity.badRequest().body("Pet not found");
        }
        petService.delete(id);
        return ResponseEntity.ok("Pet deleted successfully!");
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
