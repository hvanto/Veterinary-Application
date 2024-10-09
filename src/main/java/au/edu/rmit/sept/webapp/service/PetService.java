package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;
    

    public Pet getPetById(Long petId) {
        return petRepository.findById(petId).orElse(null);
    }

    public List<Pet> getPetsByUserId(Long userId) {
        return petRepository.findByUserId(userId);
    }

    public List<Pet> getPetsByName(String name) {
        return petRepository.findByName(name);
    }

    public void save(Pet pet) {
        petRepository.save(pet);
    }

    public void delete(Long petId) {
        petRepository.deleteById(petId);
    }

    public List<Pet> getPetsByVeteterinarianId(Long veterinarianId) {
        return petRepository.findByVeterinarianId(veterinarianId);
    }
}
