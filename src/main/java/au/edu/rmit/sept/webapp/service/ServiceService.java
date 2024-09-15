package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // Use the fully qualified name to avoid conflict
    public List<au.edu.rmit.sept.webapp.model.Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Optional<au.edu.rmit.sept.webapp.model.Service> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }
}
