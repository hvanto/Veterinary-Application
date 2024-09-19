package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Clinic;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.repository.ClinicRepository;
import au.edu.rmit.sept.webapp.repository.VeterinarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClinicService {

    private final ClinicRepository clinicRepository;
    public final VeterinarianRepository veterinarianRepository;

    @Autowired
    public ClinicService(ClinicRepository clinicRepository, VeterinarianRepository veterinarianRepository) {
        this.clinicRepository = clinicRepository;
        this.veterinarianRepository = veterinarianRepository;
    }

    public List<Clinic> getAllClinics() {
        return clinicRepository.findAll();
    }

    public Optional<Clinic> getClinicById(Long id) {
        return clinicRepository.findById(id);
    }

    // Method to retrieve unique services provided by veterinarians of a clinic
    public Set<au.edu.rmit.sept.webapp.model.Service> getServicesByClinicId(Long clinicId) {
        // Find all veterinarians associated with the clinic ID
        List<Veterinarian> veterinarians = veterinarianRepository.findAllByClinic_Id(clinicId);

        // Extract and return unique services provided by these veterinarians
        return veterinarians.stream()
                .flatMap(vet -> vet.getServices().stream())
                .collect(Collectors.toSet()); // Using Set to ensure uniqueness
    }
}

