package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.WeightRecord;
import au.edu.rmit.sept.webapp.repository.PetRepository;
import au.edu.rmit.sept.webapp.repository.WeightRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the WeightRecordService class.
 */
@SpringBootTest
public class WeightRecordServiceTests {

    @Autowired
    private WeightRecordService weightRecordService;

    @MockBean
    private WeightRecordRepository weightRecordRepository;

    @Autowired
    private PetRepository petRepository;

    private WeightRecord weightRecord1;
    private WeightRecord weightRecord2;

    private Pet testPet;

    /**
     * Sets up test data before each test case.
     */
    @BeforeEach
    public void setUp() {
        // Create and save a test pet
        testPet = new Pet();
        testPet.setName("Buddy");
        petRepository.save(testPet);

        // Create test weight records
        weightRecord1 = new WeightRecord();
        weightRecord1.setId(1L);
        weightRecord1.setPet(testPet);
        weightRecord1.setWeight(10.5);
        weightRecord1.setRecordDate(new Date());

        weightRecord2 = new WeightRecord();
        weightRecord2.setId(2L);
        weightRecord2.setPet(testPet);
        weightRecord2.setWeight(12.0);
        weightRecord2.setRecordDate(new Date());
    }

    /**
     * Test case for retrieving weight records by pet ID.
     */
    @Test
    public void testGetWeightRecordsByPetId() {
        // Set up the repository mock to return the test data
        when(weightRecordRepository.findByPetId(100L)).thenReturn(Arrays.asList(weightRecord1, weightRecord2));

        // Call the service method
        List<WeightRecord> weightRecords = weightRecordService.getWeightRecordsByPetId(100L);

        // Verify the results
        assertNotNull(weightRecords);
        assertEquals(2, weightRecords.size());
        assertEquals(10.5, weightRecords.get(0).getWeight());
        assertEquals(12.0, weightRecords.get(1).getWeight());

        // Verify that the repository was called once
        verify(weightRecordRepository, times(1)).findByPetId(100L);
    }

    /**
     * Test case for retrieving weight records by pet ID when no records exist.
     */
    @Test
    public void testGetWeightRecordsByPetId_Empty() {
        // Set up the repository mock to return an empty list
        when(weightRecordRepository.findByPetId(200L)).thenReturn(List.of());

        // Call the service method
        List<WeightRecord> weightRecords = weightRecordService.getWeightRecordsByPetId(200L);

        // Verify the results
        assertNotNull(weightRecords);
        assertTrue(weightRecords.isEmpty());

        // Verify that the repository was called once
        verify(weightRecordRepository, times(1)).findByPetId(200L);
    }

    /**
     * Test case for saving a new weight record.
     */
    @Test
    public void testSaveNewWeightRecord() {
        // Create a new weight record
        WeightRecord newWeightRecord = new WeightRecord();
        newWeightRecord.setPet(testPet);
        newWeightRecord.setWeight(15.0);
        newWeightRecord.setRecordDate(new Date());

        // Call the service method to save the record
        weightRecordService.save(newWeightRecord);

        // Verify that the repository's save method was called with the new record
        verify(weightRecordRepository, times(1)).save(newWeightRecord);
    }

    /**
     * Test case for updating an existing weight record.
     */
    @Test
    public void testUpdateWeightRecord() {
        // Modify weightRecord1
        weightRecord1.setWeight(11.0);

        // Call the service method to save the updated record
        weightRecordService.save(weightRecord1);

        // Verify that the repository's save method was called with the updated record
        verify(weightRecordRepository, times(1)).save(weightRecord1);
    }
}
