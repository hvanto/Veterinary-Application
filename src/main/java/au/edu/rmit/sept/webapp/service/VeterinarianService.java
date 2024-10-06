package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Clinic;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.repository.VeterinarianRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinarianService {
    private final VeterinarianRepository veterinarianRepository;
    private final ClinicService clinicService;
    private final EncryptionService encryptionService;

    public VeterinarianService(VeterinarianRepository veterinarianRepository, EncryptionService encryptionService, ClinicService clinicService) {
        this.veterinarianRepository = veterinarianRepository;
        this.encryptionService = encryptionService;
        this.clinicService = clinicService;
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

    public Veterinarian validateVeterinarianCredentials(String email, String plainPassword, String clinicName) throws Exception {
        Optional<Veterinarian> veterinarianOptional;

        // Scenario 1: If clinicName is provided, find the clinic by its name
        if (clinicName != null && !clinicName.trim().isEmpty()) {
            Clinic clinic = clinicService.getClinicByName(clinicName);

            // Check if the clinic exists
            if (clinic == null) {
                throw new Exception("Invalid credentials: Clinic not found.");
            }

            // Find the veterinarian by email and clinic ID
            veterinarianOptional = veterinarianRepository.findByEmailAndClinic_Id(email, clinic.getId());
        } else {
            // Scenario 2: If no clinicName is provided, assume "Independent" clinic (clinicId = 1)
            Clinic independentClinic = clinicService.getClinicByName("Independent");
            if (independentClinic == null) {
                throw new Exception("Independent clinic not found.");
            }
            veterinarianOptional = veterinarianRepository.findByEmailAndClinic_Id(email, independentClinic.getId());
        }

        // Check if the veterinarian exists
        if (veterinarianOptional.isEmpty()) {
            throw new Exception("Invalid credentials: Veterinarian not found.");
        }

        Veterinarian veterinarian = veterinarianOptional.get();

        // Compare the plain password with the hashed password using BCrypt
        boolean passwordMatches = encryptionService.matchesPassword(plainPassword, veterinarian.getPassword());

        if (!passwordMatches) {
            throw new Exception("Invalid credentials: Password mismatch.");
        }

        // If credentials are valid, return the veterinarian object
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

    public boolean existsByEmail(String email) {
        return veterinarianRepository.existsByEmail(email);
    }
}