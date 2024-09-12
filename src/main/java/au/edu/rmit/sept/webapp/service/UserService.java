package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EncryptionService encryptionService;

    @Autowired
    public UserService(UserRepository userRepository, EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }

    // Save the user with a hashed password, check if email is unique
    public User saveUser(User user) throws Exception {
        // Check if the email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email already exists");
        }

        // Hash the password before saving the user
        user.setPassword(encryptionService.encryptPassword(user.getPassword()));
        return userRepository.save(user);
    }
}
