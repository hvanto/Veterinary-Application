package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import au.edu.rmit.sept.webapp.model.MedicalHistory;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.PhysicalExam;
import au.edu.rmit.sept.webapp.model.Vaccination;
import au.edu.rmit.sept.webapp.model.TreatmentPlan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import au.edu.rmit.sept.webapp.model.WeightRecord;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;


/**
 * Handles page routing and resolves the necessary view templates.
 */
@Controller
public class PageResolverController {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private PetService petService;

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
  
    /**
     * Handles the request to the home page and provides necessary data to the view.
     *
     * @param request   - The HTTP request containing the URL and query data
     * @param model     - Data object used by Thymeleaf for rendering the view
     * @return index.html page with "home" content as the main section
     */
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        model.addAttribute("content", "home");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        return "index";
    }

    /**
     * Handles the request to the home page and provides necessary data to the view.
     *
     * @param request   - The HTTP request containing the URL and query data
     * @param model     - Data object used by Thymeleaf for rendering the view
     * @return index.html page with "home" content as the main section
     */
    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        model.addAttribute("content", "home");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        return "index";
    }


    @GetMapping("/book-appointment")
    public String bookAppointment(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        model.addAttribute("content", "book-appointment");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        return "index";
    }

    @GetMapping("/signup")
    public String signup(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        model.addAttribute("content", "signup");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        return "index";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        model.addAttribute("content", "loginUser");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        return "index";
    }
    /**
     * Displays the medical records page for the selected pet.
     * If no pet is selected, a dropdown list of the user's pets is displayed.
     * @param selectedPetId The ID of the selected pet.
     * @param model Model to hold the data for the view.
     * @return Medical records view or pet selection dropdown.
     */
    @GetMapping("/medical-records")
    public String getMedicalRecords(@RequestParam(required = false) Long selectedPetId, Model model) {
        Long userId = 1L;  // Hardcoded userId for testing purposes

        // Fetch all pets for the dropdown list based on the user ID
        List<Pet> petList = petService.getPetsByUserId(userId);
        model.addAttribute("petList", petList);

        // If no pet is selected, show the list of pet cards
        if (selectedPetId == null) {
            model.addAttribute("message", "Please select a pet to view medical records.");
            model.addAttribute("content", "medical-records");
            return "index";
        }

        // Fetch selected pet information
        Pet selectedPet = petService.getPetById(selectedPetId);
        if (selectedPet == null) {
            model.addAttribute("errorMessage", "No pet found with this ID.");
            model.addAttribute("content", "medical-records");
            return "index";
        }

        // Fetch medical history, physical exams, vaccinations, and treatment plans for the selected pet
        List<MedicalHistory> medicalHistoryList = medicalHistoryService.getMedicalHistoryByPetId(selectedPetId);
        List<PhysicalExam> physicalExamList = physicalExamService.getPhysicalExamsByPetId(selectedPetId);
        List<Vaccination> vaccinationList = vaccinationService.getVaccinationsByPetId(selectedPetId);
        List<WeightRecord> weightRecords = weightRecordService.getWeightRecordsByPetId(selectedPetId);
        List<TreatmentPlan> treatmentPlanList = treatmentPlanService.getTreatmentPlansByPetId(selectedPetId);

        // Add selected pet, medical history, physical exams, vaccinations, and treatment plans to the model
        model.addAttribute("selectedPet", selectedPet);
        model.addAttribute("medicalHistoryList", medicalHistoryList);
        model.addAttribute("physicalExamList", physicalExamList);
        model.addAttribute("weightRecords", weightRecords);
        model.addAttribute("vaccinationList", vaccinationList);
        model.addAttribute("treatmentPlanList", treatmentPlanList);
        model.addAttribute("content", "medical-records");

        return "index";
    }
  
    @GetMapping("/userProfile")
    public String userProfile(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        model.addAttribute("content", "userProfile");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);
    }
  
    /**
     * Fetches weight records for a selected pet via API.
     * @param selectedPetId The ID of the selected pet.
     * @return List of weight records for the pet.
     */
    @GetMapping("/api/weight-records")
    @ResponseBody
    public List<WeightRecord> getWeightRecords(@RequestParam("selectedPetId") Long selectedPetId) {
        return weightRecordService.getWeightRecordsByPetId(selectedPetId);
    }

    /**
     * Generates and downloads medical records in PDF or XML format.
     * @param selectedPetId The ID of the selected pet.
     * @param format The format of the file (pdf or xml).
     * @param sections The sections of the records to include.
     * @return Response with the downloadable file.
     */
    @GetMapping("/download-medical-records")
    public ResponseEntity<InputStreamResource> downloadMedicalRecords(
            @RequestParam("selectedPetId") Long selectedPetId,
            @RequestParam("format") String format,
            @RequestParam("sections") List<String> sections) {

        // Fetch selected pet
        Pet selectedPet = petService.getPetById(selectedPetId);
        if (selectedPet == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Fetch related medical records
        List<MedicalHistory> medicalHistoryList = medicalHistoryService.getMedicalHistoryByPetId(selectedPetId);
        List<PhysicalExam> physicalExamList = physicalExamService.getPhysicalExamsByPetId(selectedPetId);
        List<Vaccination> vaccinationList = vaccinationService.getVaccinationsByPetId(selectedPetId);
        List<TreatmentPlan> treatmentPlanList = treatmentPlanService.getTreatmentPlansByPetId(selectedPetId);
        List<WeightRecord> weightRecordList = weightRecordService.getWeightRecordsByPetId(selectedPetId);

        // Generate the file based on the requested format (PDF or XML)
        ByteArrayInputStream inputStream;
        if ("pdf".equalsIgnoreCase(format)) {
            inputStream = fileGenerationService.generatePDF(selectedPet, medicalHistoryList, physicalExamList, vaccinationList, treatmentPlanList, weightRecordList, sections);
        } else if ("xml".equalsIgnoreCase(format)) {
            inputStream = fileGenerationService.generateXML(selectedPet, medicalHistoryList, physicalExamList, vaccinationList, treatmentPlanList, weightRecordList, sections);
        } else {
            return ResponseEntity.badRequest().body(null); // Return bad request for invalid format
        }

        // Set the file name and the response headers
        String filename = "medical_records_" + selectedPet.getName() + "." + format;

        // Return the file in the response with appropriate headers
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType("pdf".equalsIgnoreCase(format) ? MediaType.APPLICATION_PDF : MediaType.APPLICATION_XML)
                .body(new InputStreamResource(inputStream));
    }
    // @GetMapping("/prescription")
    // public String prescription(HttpServletRequest request, Model model) {
    //     String requestURL = request.getRequestURL().toString();
    //     String queryString = request.getQueryString();

    //     model.addAttribute("content", "prescription");
    //     model.addAttribute("url", requestURL);
    //     model.addAttribute("queryString", queryString);

    //     return "index";
    // }

    // @GetMapping("/medical-records")
    // public String medicalRecords(HttpServletRequest request, Model model) {
    //     String requestURL = request.getRequestURL().toString();
    //     String queryString = request.getQueryString();

    //     model.addAttribute("content", "medical-records");
    //     model.addAttribute("url", requestURL);
    //     model.addAttribute("queryString", queryString);

    //     return "index";
    // }
}
