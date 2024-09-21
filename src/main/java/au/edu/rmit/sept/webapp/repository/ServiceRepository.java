package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    // Custom query methods if needed
}
