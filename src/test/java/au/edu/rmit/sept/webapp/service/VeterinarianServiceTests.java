package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Clinic;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.repository.VeterinarianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class VeterinarianServiceTests {

    @Autowired
    private VeterinarianService veterinarianService;

    @MockBean
    private VeterinarianRepository veterinarianRepository;

    @MockBean
    private ClinicService clinicService;

    @MockBean
    private EncryptionService encryptionService;

    private Veterinarian vet;
    private Clinic clinic;

    @BeforeEach
    public void setUp() {
        // Setup the veterinarian and clinic objects
        vet = new Veterinarian();
        vet.setFirstName("John");
        vet.setLastName("Doe");
        vet.setEmail("john.doe@clinic.com");
        vet.setPassword("hashed_password");

        clinic = new Clinic();
        clinic.setId(1L);
        clinic.setName("Independent");
    }

    @Test
    public void validateVeterinarianCredentials_Success() throws Exception {
        // Mock clinic and veterinarian lookups
        Mockito.when(clinicService.getClinicByName(anyString())).thenReturn(clinic);
        Mockito.when(veterinarianRepository.findByEmailAndClinic_Id(anyString(), anyLong())).thenReturn(Optional.of(vet));
        Mockito.when(encryptionService.matchesPassword("password123", "hashed_password")).thenReturn(true);

        // Call the service method
        Veterinarian result = veterinarianService.validateVeterinarianCredentials("john.doe@clinic.com", "password123", "Independent");

        // Verify that the veterinarian is returned
        assertEquals(vet, result);
    }

    @Test
    public void validateVeterinarianCredentials_InvalidPassword() {
        // Mock clinic and veterinarian lookups
        Mockito.when(clinicService.getClinicByName(anyString())).thenReturn(clinic);
        Mockito.when(veterinarianRepository.findByEmailAndClinic_Id(anyString(), anyLong())).thenReturn(Optional.of(vet));
        Mockito.when(encryptionService.matchesPassword("wrongPassword", "hashed_password")).thenReturn(false);

        // Test the invalid password scenario
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            veterinarianService.validateVeterinarianCredentials("john.doe@clinic.com", "wrongPassword", "Independent");
        });

        assertEquals("Invalid credentials: Password mismatch.", exception.getMessage());
    }

    @Test
    public void validateVeterinarianCredentials_VeterinarianNotFound() {
        // Mock clinic lookup but return empty for veterinarian
        Mockito.when(clinicService.getClinicByName(anyString())).thenReturn(clinic);
        Mockito.when(veterinarianRepository.findByEmailAndClinic_Id(anyString(), anyLong())).thenReturn(Optional.empty());

        // Test the veterinarian not found scenario
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            veterinarianService.validateVeterinarianCredentials("nonexistent@clinic.com", "password123", "Independent");
        });

        assertEquals("Invalid credentials: Veterinarian not found.", exception.getMessage());
    }

    @Test
    public void validateVeterinarianCredentials_ClinicNotFound() {
        // Mock clinic lookup to return null
        Mockito.when(clinicService.getClinicByName(anyString())).thenReturn(null);

        // Test the clinic not found scenario
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            veterinarianService.validateVeterinarianCredentials("john.doe@clinic.com", "password123", "NonExistentClinic");
        });

        assertEquals("Invalid credentials: Clinic not found.", exception.getMessage());
    }

    @Test
    public void saveVeterinarian_Success() {
        // Setup: Set the password on the vet object
        vet.setPassword("password123");

        // Mock password encryption
        Mockito.when(encryptionService.encryptPassword("password123")).thenReturn("hashed_password");

        // Mock save operation
        Mockito.when(veterinarianRepository.save(vet)).thenReturn(vet);

        // Call the service method
        Veterinarian savedVet = veterinarianService.saveVeterinarian(vet);

        // Verify that the saved veterinarian has the hashed password
        assertEquals("hashed_password", savedVet.getPassword());

        // Additionally, verify that the save method was called
        Mockito.verify(veterinarianRepository).save(vet);
    }

    @Test
    public void updateVeterinarian_VeterinarianNotFound() {
        // Mock finding the veterinarian as empty
        Mockito.when(veterinarianRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Test the veterinarian not found scenario
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            veterinarianService.updateVeterinarian(vet);
        });

        assertEquals("Veterinarian not found", exception.getMessage());
    }

    @Test
    public void updatePassword_VeterinarianNotFound() {
        // Mock finding the veterinarian as empty
        Mockito.when(veterinarianRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Test the veterinarian not found scenario
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            veterinarianService.updatePassword(1L, "newPassword123");
        });

        assertEquals("Veterinarian not found", exception.getMessage());
    }
}
