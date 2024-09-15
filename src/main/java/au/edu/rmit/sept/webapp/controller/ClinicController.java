package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Clinic;
import au.edu.rmit.sept.webapp.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clinic")
public class ClinicController {
    private ClinicService clinicService;

    @Autowired
    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    // Get all clinics
    @PostMapping("/all")
    public ResponseEntity<?> getAllClinics() {
        try {
            List<Clinic> clinics = clinicService.getAllClinics();
            return ResponseEntity.ok(clinics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get clinic by ID
    @PostMapping("/{clinicID}")
    public ResponseEntity<?> getClinic(@PathVariable Long clinicID) {
        try {
            Optional<Clinic> clinic = clinicService.getClinicById(clinicID);
            return ResponseEntity.ok(clinic);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
