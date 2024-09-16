package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EncryptionService encryptionService;

    @Autowired
    public UserService(UserRepository userRepository, EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }

    // Check if the email already exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Save the user with a hashed password
    public User saveUser(User user) {
        user.setPassword(encryptionService.encryptPassword(user.getPassword()));
        return userRepository.save(user);
    }

    // Find a user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find first user in table
    public Optional<User> findFirst() {
        return userRepository.findFirstByOrderByIdAsc();
    }

    // Validate user login
    public boolean validateUserCredentials(String email, String plainPassword) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new Exception("Email not found");
        }

        User user = userOptional.get();
        // Check if password matches
        if (!encryptionService.matchesPassword(plainPassword, user.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        return true;
    }
}
