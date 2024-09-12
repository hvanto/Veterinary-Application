package au.edu.rmit.sept.webapp.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Method to hash the password
    public String encryptPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    // Method to check if the plain password matches the hashed password
    public boolean matchesPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}
