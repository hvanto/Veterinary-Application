package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Vaccination;
import au.edu.rmit.sept.webapp.repository.VaccinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaccinationService {

    @Autowired
    private VaccinationRepository vaccinationRepository;

    public List<Vaccination> getAllVaccinations() {
        return vaccinationRepository.findAll();
    }

    public List<Vaccination> getVaccinationsByPetId(Long petId) {
        // Fetch vaccinations directly by pet ID
        return vaccinationRepository.findByPetId(petId);
    }

    public Vaccination getVaccinationById(Long id) {
        return vaccinationRepository.findById(id).orElse(null);
    }

    public void save(Vaccination vaccination) {
        vaccinationRepository.save(vaccination);
    }

    public void deleteVaccination(Long id) {
        vaccinationRepository.deleteById(id);
    }
}
