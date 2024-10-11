package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.model.*;
import au.edu.rmit.sept.webapp.service.FileGenerationService;
import au.edu.rmit.sept.webapp.service.VeterinarianService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileGenerationServiceTests {

    @Autowired
    private FileGenerationService fileGenerationService;

    @Autowired
    private VeterinarianService veterinarianService;

    private Pet testPet;
    private List<MedicalHistory> medicalHistoryList;
    private List<PhysicalExam> physicalExamList;
    private List<Vaccination> vaccinationList;
    private List<TreatmentPlan> treatmentPlanList;
    private List<WeightRecord> weightRecordList;

    @BeforeEach
    public void setUp() {
        // Check if Dr. John exists, if not, create and save a new Veterinarian
        Veterinarian drJohn = veterinarianService.findByEmail("drjohn@clinic.com")
                .orElseGet(() -> saveVeterinarian("John", "Doe", "drjohn@clinic.com", "123456789", "password123"));

        // Create Pet and assign Dr. John
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Smith");

        testPet = new Pet();
        testPet.setId(1L);
        testPet.setName("Buddy");
        testPet.setUser(user);

        // Create mock lists
        medicalHistoryList = Collections.singletonList(new MedicalHistory(testPet, "Checkup", "Dr. John", drJohn, new java.util.Date(), "All good", null));
        physicalExamList = Collections.singletonList(new PhysicalExam(testPet, LocalDate.of(2023, 1, 1), "Dr. John", "Healthy"));
        vaccinationList = Collections.singletonList(new Vaccination(testPet, "Rabies", new java.util.Date(), "Dr. John", new java.util.Date()));
        treatmentPlanList = Collections.singletonList(new TreatmentPlan(testPet, LocalDate.of(2023, 1, 1), "Routine checkup", "Dr. John", "No issues found"));
        weightRecordList = Collections.singletonList(new WeightRecord(testPet, new java.util.Date(), 12.5));
    }

    private Veterinarian saveVeterinarian(String firstName, String lastName, String email, String contact, String password) {
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setFirstName(firstName);
        veterinarian.setLastName(lastName);
        veterinarian.setEmail(email);
        veterinarian.setContact(contact);
        veterinarian.setPassword(password);
        return veterinarianService.saveVeterinarian(veterinarian);
    }

    @Test
    public void testGenerateXML() throws IOException {
        // Test generating XML with all sections
        List<String> sections = Arrays.asList("full", "physicalExams", "vaccinations", "treatmentPlans", "weightRecords");
        ByteArrayInputStream xmlStream = fileGenerationService.generateXML(testPet, medicalHistoryList, physicalExamList, vaccinationList, treatmentPlanList, weightRecordList, sections);

        // Verify the XML content
        assertNotNull(xmlStream);
        byte[] xmlBytes = xmlStream.readAllBytes();
        assertTrue(xmlBytes.length > 0);

        // Check for specific content in the XML
        String xmlContent = new String(xmlBytes);
        assertTrue(xmlContent.contains("<Pet name=\"Buddy\">"));
        assertTrue(xmlContent.contains("<WeightRecords>"));
        assertTrue(xmlContent.contains("<MedicalHistory>"));
        assertTrue(xmlContent.contains("<PhysicalExams>"));
        assertTrue(xmlContent.contains("<Vaccinations>"));
        assertTrue(xmlContent.contains("<TreatmentPlans>"));
    }

    @Test
    public void testGenerateXML_EmptySections() throws IOException {
        // Test generating XML with no sections
        List<String> sections = Collections.emptyList();
        ByteArrayInputStream xmlStream = fileGenerationService.generateXML(testPet, medicalHistoryList, physicalExamList, vaccinationList, treatmentPlanList, weightRecordList, sections);

        // Verify the XML content
        assertNotNull(xmlStream);
        byte[] xmlBytes = xmlStream.readAllBytes();
        assertTrue(xmlBytes.length > 0);

        // Check that the XML does not contain specific sections
        String xmlContent = new String(xmlBytes);
        assertTrue(xmlContent.contains("<Pet name=\"Buddy\">"));
        assertFalse(xmlContent.contains("<WeightRecords>"));
        assertFalse(xmlContent.contains("<MedicalHistory>"));
        assertFalse(xmlContent.contains("<PhysicalExams>"));
        assertFalse(xmlContent.contains("<Vaccinations>"));
        assertFalse(xmlContent.contains("<TreatmentPlans>"));
    }
}
