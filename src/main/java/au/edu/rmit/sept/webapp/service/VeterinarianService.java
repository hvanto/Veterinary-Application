package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.repository.VeterinarianRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VeterinarianService {
    private final VeterinarianRepository veterinarianRepository;

    public VeterinarianService(VeterinarianRepository veterinarianRepository) {
        this.veterinarianRepository = veterinarianRepository;
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
}
