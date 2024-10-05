package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.repository.VeterinarianRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinarianService {
    private final VeterinarianRepository veterinarianRepository;
    private final EncryptionService encryptionService;

    public VeterinarianService(VeterinarianRepository veterinarianRepository, EncryptionService encryptionService) {
        this.veterinarianRepository = veterinarianRepository;
        this.encryptionService = encryptionService;
    }

    public List<Veterinarian> getAllVeterinarians() {
        return veterinarianRepository.findAll();
    }

    public List<Veterinarian> getVeterinariansByClinicId(Long clinicId) {
        return veterinarianRepository.findAllByClinic_Id(clinicId);
    }

    public List<Veterinarian> getVeterinariansByServiceId(Long serviceId) {
        return veterinarianRepository.findAllByServices_Id(serviceId);
    }

    public List<Veterinarian> getVeterinariansByClinicIdAndServiceId(Long clinicId, Long serviceId) {
        return veterinarianRepository.findAllByClinic_IdAndServices_Id(clinicId, serviceId);
    }

    public Optional<Veterinarian> getVeterinarianById(Long id) {
        return veterinarianRepository.findById(id);
    }


    public List<au.edu.rmit.sept.webapp.model.Service> getServicesByVeterinarianId(Long veterinarianId) {
        Optional<Veterinarian> veterinarian = veterinarianRepository.findVeterinarianWithServicesById(veterinarianId);
        // Handle this properly in your controller by returning a proper error message
        return veterinarian.map(Veterinarian::getServices).orElse(null);
    }

    // Check if the email already exists for a veterinarian
    public boolean emailExists(String email) {
        return veterinarianRepository.existsByEmail(email);
    }

    // Save the veterinarian with a hashed password
    public Veterinarian saveVeterinarian(Veterinarian veterinarian) {
        veterinarian.setPassword(encryptionService.encryptPassword(veterinarian.getPassword()));
        return veterinarianRepository.save(veterinarian);
    }

    public Veterinarian validateVeterinarianCredentials(String email, String plainPassword, Long clinicId) throws Exception {
        Optional<Veterinarian> veterinarianOptional;

        // If clinicId is provided, use it to find the veterinarian; otherwise, check only by email
        if (clinicId != null) {
            veterinarianOptional = veterinarianRepository.findByEmailAndClinic_Id(email, clinicId);
        } else {
            veterinarianOptional = veterinarianRepository.findByEmail(email); // For "Independent" clinic or when clinicId is not required
        }

        if (veterinarianOptional.isEmpty()) {
            throw new Exception("Invalid credentials or clinic");
        }

        Veterinarian veterinarian = veterinarianOptional.get();

        // Check if password matches (encrypted)
        if (!encryptionService.matchesPassword(plainPassword, veterinarian.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        return veterinarian;
    }

    // Update veterinarian details
    public Veterinarian updateVeterinarian(Veterinarian updatedVeterinarian) throws Exception {
        Optional<Veterinarian> existingVeterinarianOptional = veterinarianRepository.findById(updatedVeterinarian.getId());

        if (existingVeterinarianOptional.isEmpty()) {
            throw new Exception("Veterinarian not found");
        }

        Veterinarian existingVeterinarian = existingVeterinarianOptional.get();

        // Update veterinarian details
        existingVeterinarian.setFirstName(updatedVeterinarian.getFirstName());
        existingVeterinarian.setLastName(updatedVeterinarian.getLastName());
        existingVeterinarian.setEmail(updatedVeterinarian.getEmail());
        existingVeterinarian.setContact(updatedVeterinarian.getContact());

        // Save the updated veterinarian details
        return veterinarianRepository.save(existingVeterinarian);
    }

    // Update veterinarian password with hashing
    public void updatePassword(Long veterinarianId, String newPassword) throws Exception {
        Optional<Veterinarian> veterinarianOptional = veterinarianRepository.findById(veterinarianId);
        if (veterinarianOptional.isEmpty()) {
            throw new Exception("Veterinarian not found");
        }

        Veterinarian veterinarian = veterinarianOptional.get();
        veterinarian.setPassword(encryptionService.encryptPassword(newPassword));
        veterinarianRepository.save(veterinarian);
    }

    // Find a veterinarian by email
    public Optional<Veterinarian> findByEmail(String email) {
        return veterinarianRepository.findByEmail(email);
    }
}