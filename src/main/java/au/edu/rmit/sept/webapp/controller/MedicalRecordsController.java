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

        // Seed Physical Exams (Adding more physical exams for each pet)
        PhysicalExam exam1 = new PhysicalExam(pet1, LocalDate.of(2023, 1, 15), "John Doe", "All good, minor dental issues");
        PhysicalExam exam2 = new PhysicalExam(pet2, LocalDate.of(2023, 4, 5), "Sarah Smith", "Slight weight loss, nothing critical");
        PhysicalExam exam3 = new PhysicalExam(pet1, LocalDate.of(2023, 6, 10), "Sarah Smith", "Routine checkup, no issues found");
        PhysicalExam exam4 = new PhysicalExam(pet2, LocalDate.of(2023, 8, 20), "John Doe", "Minor allergic reaction observed");
        physicalExamService.save(exam1);
        physicalExamService.save(exam2);
        physicalExamService.save(exam3);
        physicalExamService.save(exam4);

        // Convert LocalDate to Date
        LocalDate dummyLocalDate = LocalDate.of(1970, 1, 1);
        Date dummyDate = Date.from(dummyLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Create a "next day" dummy date
        LocalDate nextDayLocalDate = LocalDate.now().plusDays(1); // This gets the next day's date
        Date nextDayDate = Date.from(nextDayLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Seed Vaccinations with dummy date (Adding more vaccination records for each pet)
        Vaccination vaccination1 = new Vaccination(pet1, "Rabies", dummyDate, "John Doe", nextDayDate);
        Vaccination vaccination2 = new Vaccination(pet2, "Distemper", dummyDate, "Sarah Smith", nextDayDate);
        Vaccination vaccination3 = new Vaccination(pet1, "Parvovirus", dummyDate, "John Doe", nextDayDate);
        Vaccination vaccination4 = new Vaccination(pet2, "Leptospirosis", dummyDate, "Sarah Smith", nextDayDate);
        vaccinationService.save(vaccination1);
        vaccinationService.save(vaccination2);
        vaccinationService.save(vaccination3);
        vaccinationService.save(vaccination4);

        // Convert LocalDate to Date for weight records (Add more weight records)
        Date weightRecordDate1 = Date.from(LocalDate.of(2023, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date weightRecordDate2 = Date.from(LocalDate.of(2023, 2, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date weightRecordDate3 = Date.from(LocalDate.of(2023, 3, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date weightRecordDate4 = Date.from(LocalDate.of(2023, 4, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date weightRecordDate5 = Date.from(LocalDate.of(2023, 5, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date weightRecordDate6 = Date.from(LocalDate.of(2023, 6, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date weightRecordDate7 = Date.from(LocalDate.of(2023, 7, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date weightRecordDate8 = Date.from(LocalDate.of(2023, 8, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Seed Weight Records with converted dates (adding more weight records)
        WeightRecord weight1 = new WeightRecord(pet1, weightRecordDate1, 5.2);
        WeightRecord weight2 = new WeightRecord(pet2, weightRecordDate2, 5.5);
        WeightRecord weight3 = new WeightRecord(pet1, weightRecordDate3, 5.4);
        WeightRecord weight4 = new WeightRecord(pet2, weightRecordDate4, 5.8);
        WeightRecord weight5 = new WeightRecord(pet1, weightRecordDate5, 5.6);
        WeightRecord weight6 = new WeightRecord(pet2, weightRecordDate6, 5.7);
        WeightRecord weight7 = new WeightRecord(pet1, weightRecordDate7, 5.8);
        WeightRecord weight8 = new WeightRecord(pet2, weightRecordDate8, 5.9);

        // Save the records
        weightRecordService.save(weight1);
        weightRecordService.save(weight2);
        weightRecordService.save(weight3);
        weightRecordService.save(weight4);
        weightRecordService.save(weight5);
        weightRecordService.save(weight6);
        weightRecordService.save(weight7);
        weightRecordService.save(weight8);

        TreatmentPlan plan1 = new TreatmentPlan(pet1, nextDayLocalDate, "Arthritis Management", "John Doe", "None");  // Ongoing arthritis management
        TreatmentPlan plan2 = new TreatmentPlan(pet2, nextDayLocalDate, "Allergy Treatment", "Adams Mark", "None");   // Ongoing allergy treatment
        TreatmentPlan plan3 = new TreatmentPlan(pet1, nextDayLocalDate, "Scheduled Surgery for Joint Pain", "Sarah Smith", "None"); // Upcoming surgery
        TreatmentPlan plan4 = new TreatmentPlan(pet2, nextDayLocalDate, "Scheduled Physical Therapy", "Sarah Smith", "None"); //

        treatmentPlanService.save(plan1);
        treatmentPlanService.save(plan2);
        treatmentPlanService.save(plan3);
        treatmentPlanService.save(plan4);

        // Convert LocalDate to Date for medical history event dates (Add more medical history records related to actual medical conditions)
        Date historyDate1 = Date.from(LocalDate.of(2023, 1, 25).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date historyDate2 = Date.from(LocalDate.of(2023, 3, 5).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date historyDate3 = Date.from(LocalDate.of(2023, 5, 12).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date historyDate4 = Date.from(LocalDate.of(2023, 6, 22).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date historyDate5 = Date.from(LocalDate.of(2023, 8, 10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date historyDate6 = Date.from(LocalDate.of(2023, 9, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());

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

        // Seed Medical History related to illnesses, injuries, or fever
        MedicalHistory history1 = new MedicalHistory(pet1, "John Doe", "Fever", drJohn, historyDate1, "Fever observed, prescribed antipyretics", null);
        MedicalHistory history2 = new MedicalHistory(pet2, "Sarah Smith", "Ear Infection", drSarah, historyDate2, "Ear infection treated with antibiotics", null);
        MedicalHistory history3 = new MedicalHistory(pet1, "Karl Malus", "Sprained leg", drSarah, historyDate3, "Prescribed rest and anti-inflammatory medication", null);
        MedicalHistory history4 = new MedicalHistory(pet2, "Stephen Strange", "Diarrhea", drJohn, historyDate4, "Administered fluids, prescribed probiotics", null);
        MedicalHistory history5 = new MedicalHistory(pet1, "John Doe", "Vomiting", drJohn, historyDate5, "Prescribed antiemetic medication", null);
        MedicalHistory history6 = new MedicalHistory(pet2, "Sarah Smith", "Skin rash", drSarah, historyDate6, "Applied topical treatment for the rash", null);

        // Save the medical history records
        medicalHistoryService.save(history1);
        medicalHistoryService.save(history2);
        medicalHistoryService.save(history3);
        medicalHistoryService.save(history4);
        medicalHistoryService.save(history5);
        medicalHistoryService.save(history6);
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
            @RequestParam(value = "examDate", required = false) LocalDate examDate) {
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
                        TreatmentPlan treatmentPlan = new TreatmentPlan(pet, planDate, description, practitioner, notes);
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
