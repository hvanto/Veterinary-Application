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

    public List<Veterinarian> findAll() {
        return veterinarianRepository.findAll();
    }

    public Optional<Veterinarian> findById(Long id) {
        return veterinarianRepository.findById(id);
    }

    public Veterinarian save(Veterinarian veterinarian) {
        return veterinarianRepository.save(veterinarian);
    }

    public void delete(Long id) {
        veterinarianRepository.deleteById(id);
    }
}
