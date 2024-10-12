package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Vaccination;
import au.edu.rmit.sept.webapp.repository.VaccinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Vaccination entities.
 * This service handles the business logic related to fetching and saving vaccination records
 * for pets in the system.
 */
@Service
public class VaccinationService {

    // Autowired repository to handle CRUD operations for Vaccination entities
    @Autowired
    private VaccinationRepository vaccinationRepository;

    /**
     * Retrieves a list of vaccination records for a specific pet.
     *
     * @param petId The ID of the pet whose vaccination records are to be retrieved.
     * @return A list of Vaccination objects associated with the specified pet.
     */
    public List<Vaccination> getVaccinationsByPetId(Long petId) {
        // Fetch vaccinations by pet ID
        return vaccinationRepository.findByPetId(petId);
    }

    /**
     * Saves a new vaccination record or updates an existing one in the database.
     *
     * @param vaccination The Vaccination object to be saved.
     */
    public void save(Vaccination vaccination) {
        vaccinationRepository.save(vaccination);
    }
}
