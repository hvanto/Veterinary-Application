package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.PhysicalExam;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.repository.PhysicalExamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the PhysicalExamService class. This class tests the methods
 * related to managing PhysicalExam entities.
 */
public class PhysicalExamServiceTests {

    @InjectMocks
    private PhysicalExamService physicalExamService;

    @Mock
    private PhysicalExamRepository physicalExamRepository;

    private PhysicalExam physicalExam1;
    private PhysicalExam physicalExam2;
    private Pet testPet;

    /**
     * Sets up the test data and initializes mocks before each test.
     */
    @BeforeEach
    public void setUp() {
        // Initialize Mockito annotations to create mocks
        MockitoAnnotations.openMocks(this);

        // Create a test pet
        testPet = new Pet();
        testPet.setId(1L);
        testPet.setName("Buddy");

        // Create two physical exams associated with the test pet
        physicalExam1 = new PhysicalExam();
        physicalExam1.setId(1L);
        physicalExam1.setPet(testPet);
        physicalExam1.setNotes("Annual Checkup");

        physicalExam2 = new PhysicalExam();
        physicalExam2.setId(2L);
        physicalExam2.setPet(testPet);
        physicalExam2.setNotes("Vaccination");
    }

    /**
     * Test case for retrieving all physical exams by a pet's ID.
     * This test ensures the correct exams are fetched for a given pet.
     */
    @Test
    public void testGetPhysicalExamsByPetId() {
        // Mock the repository to return a list of exams for the given pet ID
        when(physicalExamRepository.findByPetId(testPet.getId()))
                .thenReturn(Arrays.asList(physicalExam1, physicalExam2));

        // Call the service method
        List<PhysicalExam> exams = physicalExamService.getPhysicalExamsByPetId(testPet.getId());

        // Verify the result and the correct number of exams returned
        assertNotNull(exams);
        assertEquals(2, exams.size());
        assertEquals("Annual Checkup", exams.get(0).getNotes());
        assertEquals("Vaccination", exams.get(1).getNotes());

        // Verify that the repository method was called once
        verify(physicalExamRepository, times(1)).findByPetId(testPet.getId());
    }

    /**
     * Test case for retrieving exams by a pet ID when no exams exist.
     * This test ensures the service handles empty results gracefully.
     */
    @Test
    public void testGetPhysicalExamsByPetId_EmptyList() {
        // Mock the repository to return an empty list when no exams are found
        when(physicalExamRepository.findByPetId(testPet.getId()))
                .thenReturn(Arrays.asList());

        // Call the service method
        List<PhysicalExam> exams = physicalExamService.getPhysicalExamsByPetId(testPet.getId());

        // Verify that the result is an empty list
        assertNotNull(exams);
        assertTrue(exams.isEmpty());

        // Verify that the repository method was called once
        verify(physicalExamRepository, times(1)).findByPetId(testPet.getId());
    }

    /**
     * Test case for saving a new physical exam.
     * This test ensures that the service can save a new exam to the database.
     */
    @Test
    public void testSaveNewPhysicalExam() {
        // Create a new physical exam
        PhysicalExam newExam = new PhysicalExam();
        newExam.setId(3L);
        newExam.setPet(testPet);
        newExam.setNotes("Dental Checkup");

        // Mock the repository save method
        when(physicalExamRepository.save(newExam)).thenReturn(newExam);

        // Call the service method to save the new exam
        physicalExamService.save(newExam);

        // Verify that the repository save method was called
        verify(physicalExamRepository, times(1)).save(newExam);
    }

    /**
     * Test case for updating an existing physical exam.
     * This test ensures that an existing exam can be updated and saved.
     */
    @Test
    public void testUpdatePhysicalExam() {
        // Update details of an existing exam
        physicalExam1.setNotes("Updated Annual Checkup");

        // Mock the repository save method
        when(physicalExamRepository.save(physicalExam1)).thenReturn(physicalExam1);

        // Call the service method to update the exam
        physicalExamService.save(physicalExam1);

        // Verify that the repository save method was called once
        verify(physicalExamRepository, times(1)).save(physicalExam1);
    }
}
