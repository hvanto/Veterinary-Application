package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.*;
import au.edu.rmit.sept.webapp.service.*;
import com.github.javafaker.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/medical-records")
public class MedicalRecordsController {

    @Autowired
    private PetService petService;

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private PhysicalExamService physicalExamService;

    @Autowired
    private VaccinationService vaccinationService;

    @Autowired
    private WeightRecordService weightRecordService;

    @Autowired
    private TreatmentPlanService treatmentPlanService;

    @Autowired
    private FileGenerationService fileGenerationService;

    @Autowired
    private UserService userService;

    @Autowired
    private VeterinarianService veterinarianService;

    @Autowired
    private AppointmentService appointmentService;

    /**
     * Fetches all pets for the logged-in user.
     * This is used to display the pet selection screen.
     * @param userId The ID of the logged-in user.
     * @return List of the user's pets.
     */
    @GetMapping("/user-pets")
    @ResponseBody
    public List<Pet> getUserPets(@RequestParam Long userId) {
        System.out.println("Received request for user pets with userId: " + userId);
        List<Pet> pets = petService.getPetsByUserId(userId);
        // Only seed data if no pets exist for the user
        if (pets.isEmpty()) {
            seedDataForUser(userId);
            pets = petService.getPetsByUserId(userId);
        }
        return pets;
    }

    /**
     * Seeds default data for a user if they have no pets.
     * @param userId The ID of the logged-in user.
     */
    private void seedDataForUser(Long userId) {
        System.out.println("Seeding data for user with ID: " + userId);

        User user = userService.findById(userId).orElse(null);

        // Seed Pets
        Pet pet1 = new Pet(user, "Buddy", "Dog", "Golden Retriever", "Male", true, "Loves to play fetch", "2_buddy_retriever.png", LocalDate.of(2018, 1, 5));
        Pet pet2 = new Pet(user, "Luna", "Dog", "Siberian Husky", "Female", true, "Very energetic and loves snow", "3_luna_husky.png", LocalDate.of(2019, 3, 10));
        petService.save(pet1);
        petService.save(pet2);

        // Seed Physical Exams
        PhysicalExam exam1 = new PhysicalExam(pet1, LocalDate.of(2023, 1, 15), "Dr. John", "All good, minor dental issues");
        PhysicalExam exam2 = new PhysicalExam(pet2, LocalDate.of(2023, 4, 5), "Dr. Sarah", "Slight weight loss, nothing critical");
        physicalExamService.save(exam1);
        physicalExamService.save(exam2);

        // Convert LocalDate to Date
        LocalDate dummyLocalDate = LocalDate.of(1970, 1, 1);
        Date dummyDate = Date.from(dummyLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Seed Vaccinations with dummy date
        Vaccination vaccination1 = new Vaccination(pet1, "Rabies", dummyDate, "Dr. John", dummyDate);
        Vaccination vaccination2 = new Vaccination(pet2, "Distemper", dummyDate, "Dr. Sarah", dummyDate);
        vaccinationService.save(vaccination1);
        vaccinationService.save(vaccination2);

        // Convert LocalDate to Date for weight records
        Date weightRecordDate1 = Date.from(LocalDate.of(2023, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date weightRecordDate2 = Date.from(LocalDate.of(2023, 2, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date weightRecordDate3 = Date.from(LocalDate.of(2023, 3, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()); // New date for weight 3
        Date weightRecordDate4 = Date.from(LocalDate.of(2023, 4, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()); // New date for weight 4
        Date weightRecordDate5 = Date.from(LocalDate.of(2023, 5, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()); // New date for weight 5
        Date weightRecordDate6 = Date.from(LocalDate.of(2023, 6, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()); // New date for weight 6

        // Seed Weight Records with converted dates
        WeightRecord weight1 = new WeightRecord(pet1, weightRecordDate1, 5.2);
        WeightRecord weight2 = new WeightRecord(pet2, weightRecordDate2, 5.5);
        WeightRecord weight3 = new WeightRecord(pet1, weightRecordDate3, 5.4);
        WeightRecord weight4 = new WeightRecord(pet2, weightRecordDate4, 5.8);
        WeightRecord weight5 = new WeightRecord(pet1, weightRecordDate5, 5.6);
        WeightRecord weight6 = new WeightRecord(pet2, weightRecordDate6, 5.7);

        // Save the records
        weightRecordService.save(weight1);
        weightRecordService.save(weight2);
        weightRecordService.save(weight3);
        weightRecordService.save(weight4);
        weightRecordService.save(weight5);
        weightRecordService.save(weight6);

        // Seed Treatment Plans
        TreatmentPlan plan1 = new TreatmentPlan(pet1, LocalDate.of(2023, 1, 10), "Routine checkup", "Dr. John", "Routine checkup, no issues found", null);
        TreatmentPlan plan2 = new TreatmentPlan(pet2, LocalDate.of(2023, 9, 12), "Allergy Treatment", "Dr. Adams", "Administered allergy medication for seasonal allergies", null);
        treatmentPlanService.save(plan1);
        treatmentPlanService.save(plan2);

        // Convert LocalDate to Date for medical history event dates
        Date historyDate1 = Date.from(LocalDate.of(2023, 1, 10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date historyDate2 = Date.from(LocalDate.of(2023, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date historyDate3 = Date.from(LocalDate.of(2023, 4, 5).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date historyDate4 = Date.from(LocalDate.of(2023, 5, 20).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Check if the veterinarians already exist, otherwise create them
        Veterinarian drJohn = veterinarianService.findByEmail("drjohn@clinic.com")
                .orElseGet(() -> {
                    Veterinarian veterinarian = new Veterinarian("John", "Doe", "drjohn@clinic.com", "123456789", "password123");
                    return veterinarianService.saveVeterinarian(veterinarian);
                });

        Veterinarian drSarah = veterinarianService.findByEmail("drsarah@clinic.com")
                .orElseGet(() -> {
                    Veterinarian veterinarian = new Veterinarian("Sarah", "Smith", "drsarah@clinic.com", "987654321", "password123");
                    return veterinarianService.saveVeterinarian(veterinarian);
                });

        // Seed Medical History using the Veterinarian objects instead of names
        MedicalHistory history1 = new MedicalHistory(pet1, "Dr. John", "Routine checkup", drJohn, historyDate1, "All good", null);
        MedicalHistory history2 = new MedicalHistory(pet1, "Dr. John", "Vaccination administered", drJohn, historyDate2, "Rabies vaccine given", null);
        MedicalHistory history3 = new MedicalHistory(pet2, "Dr. Sarah", "Dental cleaning", drSarah, historyDate3, "Teeth cleaned", null);
        MedicalHistory history4 = new MedicalHistory(pet2, "Dr. Sarah", "Allergy symptoms observed", drSarah, historyDate4, "Observed allergic reaction", null);

        // Save the medical history records
        medicalHistoryService.save(history1);
        medicalHistoryService.save(history2);
        medicalHistoryService.save(history3);
        medicalHistoryService.save(history4);
    }

    @GetMapping("/{selectedPetId}")
    @ResponseBody
    public MedicalRecordsResponse getMedicalRecords(@PathVariable Long selectedPetId) {
        Pet selectedPet = petService.getPetById(selectedPetId);
        List<MedicalHistory> medicalHistoryList = medicalHistoryService.getMedicalHistoryByPetId(selectedPetId);
        List<PhysicalExam> physicalExamList = physicalExamService.getPhysicalExamsByPetId(selectedPetId);
        List<Vaccination> vaccinationList = vaccinationService.getVaccinationsByPetId(selectedPetId);
        List<WeightRecord> weightRecords = weightRecordService.getWeightRecordsByPetId(selectedPetId);
        List<TreatmentPlan> treatmentPlanList = treatmentPlanService.getTreatmentPlansByPetId(selectedPetId);

        return new MedicalRecordsResponse(selectedPet, medicalHistoryList, physicalExamList, vaccinationList, weightRecords, treatmentPlanList);
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadMedicalRecords(
            @RequestParam("selectedPetId") Long selectedPetId,
            @RequestParam("format") String format,
            @RequestParam("sections") List<String> sections) {

        System.out.println("Selected Pet ID: " + selectedPetId);
        System.out.println("Requested format: " + format);
        System.out.println("Sections: " + sections);

        Pet selectedPet = petService.getPetById(selectedPetId);
        if (selectedPet == null) {
            System.out.println("Pet not found for ID: " + selectedPetId);
            return ResponseEntity.badRequest().body(null);
        }

        List<MedicalHistory> medicalHistoryList = medicalHistoryService.getMedicalHistoryByPetId(selectedPetId);
        List<PhysicalExam> physicalExamList = physicalExamService.getPhysicalExamsByPetId(selectedPetId);
        List<Vaccination> vaccinationList = vaccinationService.getVaccinationsByPetId(selectedPetId);
        List<TreatmentPlan> treatmentPlanList = treatmentPlanService.getTreatmentPlansByPetId(selectedPetId);
        List<WeightRecord> weightRecordList = weightRecordService.getWeightRecordsByPetId(selectedPetId);

        ByteArrayInputStream inputStream;
        if ("pdf".equalsIgnoreCase(format)) {
            inputStream = fileGenerationService.generatePDF(selectedPet, medicalHistoryList, physicalExamList, vaccinationList, treatmentPlanList, weightRecordList, sections);
        } else if ("xml".equalsIgnoreCase(format)) {
            inputStream = fileGenerationService.generateXML(selectedPet, medicalHistoryList, physicalExamList, vaccinationList, treatmentPlanList, weightRecordList, sections);
        } else {
            return ResponseEntity.badRequest().body(null);
        }

        String filename = "medical_records_" + selectedPet.getName() + "." + format;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType("pdf".equalsIgnoreCase(format) ? MediaType.APPLICATION_PDF : MediaType.APPLICATION_XML)
                .body(new InputStreamResource(inputStream));
    }

    @PostMapping("/upload-records")
    public ResponseEntity<String> uploadMedicalRecord(
            @RequestParam("appointmentId") Long appointmentId,
            @RequestParam("category") String category,
            @RequestParam("veterinarianId") Long veterinarianId,  // Veterinarian ID passed from the frontend
            @RequestParam(value = "weight", required = false) Double weight,
            @RequestParam(value = "date", required = false) LocalDate date,  // Used for weight-record, medical-history, and general
            @RequestParam(value = "vaccineName", required = false) String vaccineName,
            @RequestParam(value = "vaccinationDate", required = false) LocalDate vaccinationDate, // New field for vaccination
            @RequestParam(value = "administeredBy", required = false) String administeredBy,
            @RequestParam(value = "nextDueDate", required = false) LocalDate nextDueDate,
            @RequestParam(value = "planDate", required = false) LocalDate planDate,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "practitioner", required = false) String practitioner,
            @RequestParam(value = "notes", required = false) String notes,
            @RequestParam(value = "eventDate", required = false) LocalDate eventDate, // New field for medical-history
            @RequestParam(value = "treatment", required = false) String treatment,    // New field for medical-history
            @RequestParam(value = "examDate", required = false) LocalDate examDate,   // New field for physical-exam
            @RequestParam(value = "followUpDate", required = false) LocalDate followUpDate) {

        try {
            // Fetch the veterinarian using the passed veterinarianId
            Veterinarian vet = veterinarianService.findById(veterinarianId)
                    .orElseThrow(() -> new Exception("Veterinarian not found with ID: " + veterinarianId));

            // Fetch the appointment and associated pet
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            Pet pet = appointment.getPet();

            if (pet == null) {
                return ResponseEntity.badRequest().body("Pet not found for appointment ID: " + appointmentId);
            }

            // Handle each category as before, associating the record with the fetched veterinarian
            switch (category) {
                case "weight-record":
                    if (weight != null && date != null) {
                        WeightRecord weightRecord = new WeightRecord(pet, java.sql.Date.valueOf(date), weight);
                        weightRecordService.save(weightRecord);
                        return ResponseEntity.ok("Weight record uploaded successfully.");
                    }
                    break;

                case "vaccination":
                    if (vaccineName != null && vaccinationDate != null && administeredBy != null && nextDueDate != null) {
                        Vaccination vaccination = new Vaccination(pet, vaccineName, java.sql.Date.valueOf(vaccinationDate), administeredBy, java.sql.Date.valueOf(nextDueDate));
                        vaccinationService.save(vaccination);
                        return ResponseEntity.ok("Vaccination record uploaded successfully.");
                    }
                    break;

                case "treatment-plan":
                    if (planDate != null && description != null && practitioner != null) {
                        TreatmentPlan treatmentPlan = new TreatmentPlan(pet, planDate, description, practitioner, notes, followUpDate);
                        treatmentPlanService.save(treatmentPlan);
                        return ResponseEntity.ok("Treatment plan uploaded successfully.");
                    }
                    break;

                case "medical-history":
                    if (eventDate != null && treatment != null && practitioner != null && notes != null) {
                        MedicalHistory medicalHistory = new MedicalHistory(
                                pet, practitioner, treatment, vet, java.sql.Date.valueOf(eventDate), notes, null);
                        medicalHistoryService.save(medicalHistory);
                        return ResponseEntity.ok("Medical history uploaded successfully.");
                    }
                    break;

                case "physical-exam":
                    if (examDate != null && notes != null) {
                        PhysicalExam physicalExam = new PhysicalExam(pet, examDate, vet.getFullName(), notes);
                        physicalExamService.save(physicalExam);
                        return ResponseEntity.ok("Physical exam uploaded successfully.");
                    }
                    break;

                default:
                    return ResponseEntity.badRequest().body("Invalid category provided.");
            }

            return ResponseEntity.badRequest().body("Required fields missing for category: " + category);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the medical record upload: " + e.getMessage());
        }
    }
}
