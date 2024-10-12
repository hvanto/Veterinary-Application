package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Refill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefillRepository extends JpaRepository<Refill, Long> {
    List<Refill> findByUserId(Long userId);
}
