package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.TreatmentPlan;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.repository.TreatmentPlanRepository;
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
 * Unit tests for the TreatmentPlanService class.
 * This class tests the methods related to managing TreatmentPlan entities.
 */
public class TreatmentPlanServiceTests {

    @InjectMocks
    private TreatmentPlanService treatmentPlanService;

    @Mock
    private TreatmentPlanRepository treatmentPlanRepository;

    private TreatmentPlan treatmentPlan1;
    private TreatmentPlan treatmentPlan2;
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

        // Create two treatment plans associated with the test pet
        treatmentPlan1 = new TreatmentPlan();
        treatmentPlan1.setId(1L);
        treatmentPlan1.setPet(testPet);
        treatmentPlan1.setDescription("Vaccination Plan");

        treatmentPlan2 = new TreatmentPlan();
        treatmentPlan2.setId(2L);
        treatmentPlan2.setPet(testPet);
        treatmentPlan2.setDescription("Dental Care Plan");
    }

    /**
     * Test case for retrieving all treatment plans by a pet's ID.
     * This test ensures the correct plans are fetched for a given pet.
     */
    @Test
    public void testGetTreatmentPlansByPetId() {
        // Mock the repository to return a list of treatment plans for the given pet ID
        when(treatmentPlanRepository.findByPetId(testPet.getId()))
                .thenReturn(Arrays.asList(treatmentPlan1, treatmentPlan2));

        // Call the service method
        List<TreatmentPlan> plans = treatmentPlanService.getTreatmentPlansByPetId(testPet.getId());

        // Verify the result and the correct number of plans returned
        assertNotNull(plans);
        assertEquals(2, plans.size());
        assertEquals("Vaccination Plan", plans.get(0).getDescription());
        assertEquals("Dental Care Plan", plans.get(1).getDescription());

        // Verify that the repository method was called once
        verify(treatmentPlanRepository, times(1)).findByPetId(testPet.getId());
    }

    /**
     * Test case for retrieving treatment plans by a pet ID when no plans exist.
     * This test ensures the service handles empty results gracefully.
     */
    @Test
    public void testGetTreatmentPlansByPetId_EmptyList() {
        // Mock the repository to return an empty list when no treatment plans are found
        when(treatmentPlanRepository.findByPetId(testPet.getId()))
                .thenReturn(Arrays.asList());

        // Call the service method
        List<TreatmentPlan> plans = treatmentPlanService.getTreatmentPlansByPetId(testPet.getId());

        // Verify that the result is an empty list
        assertNotNull(plans);
        assertTrue(plans.isEmpty());

        // Verify that the repository method was called once
        verify(treatmentPlanRepository, times(1)).findByPetId(testPet.getId());
    }

    /**
     * Test case for saving a new treatment plan.
     * This test ensures that the service can save a new plan to the database.
     */
    @Test
    public void testSaveNewTreatmentPlan() {
        // Create a new treatment plan
        TreatmentPlan newPlan = new TreatmentPlan();
        newPlan.setId(3L);
        newPlan.setPet(testPet);
        newPlan.setDescription("Weight Management Plan");

        // Mock the repository save method
        when(treatmentPlanRepository.save(newPlan)).thenReturn(newPlan);

        // Call the service method to save the new plan
        treatmentPlanService.save(newPlan);

        // Verify that the repository save method was called
        verify(treatmentPlanRepository, times(1)).save(newPlan);
    }

    /**
     * Test case for updating an existing treatment plan.
     * This test ensures that an existing plan can be updated and saved.
     */
    @Test
    public void testUpdateTreatmentPlan() {
        // Update details of an existing treatment plan
        treatmentPlan1.setDescription("Updated Vaccination Plan");

        // Mock the repository save method
        when(treatmentPlanRepository.save(treatmentPlan1)).thenReturn(treatmentPlan1);

        // Call the service method to update the plan
        treatmentPlanService.save(treatmentPlan1);

        // Verify that the repository save method was called once
        verify(treatmentPlanRepository, times(1)).save(treatmentPlan1);
    }
}
