package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.VeterinarianAvailability;
import au.edu.rmit.sept.webapp.service.VeterinarianAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veterinarian-availability")
public class VeterinarianAvailabilityController {

    private final VeterinarianAvailabilityService veterinarianAvailabilityService;

    @Autowired
    public VeterinarianAvailabilityController(VeterinarianAvailabilityService veterinarianAvailabilityService) {
        this.veterinarianAvailabilityService = veterinarianAvailabilityService;
    }

    // Fetch availability by veterinarian ID
    @PostMapping("/{veterinarianId}")
    public ResponseEntity<?> getAvailabilityByVeterinarianId(@PathVariable Long veterinarianId) {
        try {
            List<VeterinarianAvailability> availability = veterinarianAvailabilityService.getAvailabilityByVeterinarianId(veterinarianId);
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

