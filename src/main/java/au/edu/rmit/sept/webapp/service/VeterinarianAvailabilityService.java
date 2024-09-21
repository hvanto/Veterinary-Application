package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.VeterinarianAvailability;
import au.edu.rmit.sept.webapp.repository.VeterinarianAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeterinarianAvailabilityService {

    private final VeterinarianAvailabilityRepository veterinarianAvailabilityRepository;

    @Autowired
    public VeterinarianAvailabilityService(VeterinarianAvailabilityRepository veterinarianAvailabilityRepository) {
        this.veterinarianAvailabilityRepository = veterinarianAvailabilityRepository;
    }

    // Fetch all availability by veterinarian ID
    public List<VeterinarianAvailability> getAvailabilityByVeterinarianId(Long veterinarianId) {
        return veterinarianAvailabilityRepository.findAllByVeterinarian_Id(veterinarianId);
    }
}
