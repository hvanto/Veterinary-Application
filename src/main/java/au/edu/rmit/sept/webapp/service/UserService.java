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
    private final NotificationService notificationService;

    @Autowired
    public UserService(UserRepository userRepository, EncryptionService encryptionService, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.notificationService = notificationService;
    }

    // Check if the email already exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Save the user with a hashed password
    public User saveUser(User user) {
        user.setPassword(encryptionService.encryptPassword(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Send welcome notification upon signup
        notificationService.createNotification(savedUser, "Welcome to VetCare! We're excited to have you.");
        
        return savedUser;
    }


    // Find a user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find first user in table
    public Optional<User> findFirst() {
        return userRepository.findFirstByOrderByIdAsc();
    }

    // Validate user login and return user object if successful
    public User validateUserCredentials(String email, String plainPassword) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new Exception("Email not found");
        }

        User user = userOptional.get();
        // Check if password matches
        if (!encryptionService.matchesPassword(plainPassword, user.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        return user;
    }

    // Update user details in the database
    public User updateUser(User updatedUser) throws Exception {
        Optional<User> existingUserOptional = userRepository.findById(updatedUser.getId());

        if (existingUserOptional.isEmpty()) {
            throw new Exception("User not found");
        }

        User existingUser = existingUserOptional.get();

        // Update user details
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setContact(updatedUser.getContact());

        // Save the updated user to the database
        return userRepository.save(existingUser);
    }

    // Update password with hashing
    public void updatePassword(Long userId, String newPassword) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new Exception("User not found");
        }

        User user = userOptional.get();
        user.setPassword(encryptionService.encryptPassword(newPassword));
        userRepository.save(user);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    // Method to delete all users
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    //  Method to update completed guide status
    public void updateCompletedGuide(Long userId, boolean completedGuide) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new Exception("User not found");
        }

        User user = userOptional.get();
        user.setCompletedGuide(completedGuide);
        userRepository.save(user);
    }
}
