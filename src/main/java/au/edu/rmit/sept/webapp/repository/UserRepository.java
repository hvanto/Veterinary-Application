package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by email
    Optional<User> findByEmail(String email);

    // Check if a user exists by email
    boolean existsByEmail(String email);
}
