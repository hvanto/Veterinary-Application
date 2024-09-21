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

    // Retrieve all vaccinations
    public List<Vaccination> getAllVaccinations() {
        return vaccinationRepository.findAll();
    }

    // Retrieve vaccinations by pet ID
    public List<Vaccination> getVaccinationsByPetId(Long petId) {
        return vaccinationRepository.findByMedicalHistoryPetId(petId);
    }

    // Retrieve a specific vaccination by ID
    public Vaccination getVaccinationById(Long id) {
        return vaccinationRepository.findById(id).orElse(null);
    }

    // Save or update a vaccination
    public Vaccination saveVaccination(Vaccination vaccination) {
        return vaccinationRepository.save(vaccination);
    }

    // Delete a vaccination by ID
    public void deleteVaccination(Long id) {
        vaccinationRepository.deleteById(id);
    }
}
