package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Clinic;
import au.edu.rmit.sept.webapp.model.Service;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/service")
public class ServiceController {
    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    // Get all services
    @PostMapping("/all")
    public ResponseEntity<?> getAllServices() {
        try {
            List<Service> services = serviceService.getAllServices();

            // Iterate over the services and remove veterinarians
            services.forEach(service -> service.setVeterinarians(null));

            return ResponseEntity.ok(services);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{serviceID}")
    public ResponseEntity<?> getService(@PathVariable Long serviceID) {
        try {
            Optional<Service> service = serviceService.getServiceById(serviceID);
            return ResponseEntity.ok(service);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get service by ID and filter veterinarians by clinic ID
//    @PostMapping("/{serviceId}/veterinarians")
//    public ResponseEntity<?> getServiceWithFilteredVeterinarians(
//            @PathVariable Long serviceId, @RequestBody Long clinicId) {
//        try {
//            // Fetch the service by ID
//            Optional<Service> service = serviceService.getServiceById(serviceId);
//
//            // Filter veterinarians by clinic ID
//            List<Veterinarian> filteredVeterinarians = service.getVeterinarians().stream()
//                    .filter(vet -> vet.getClinic().getId().equals(clinicId))
//                    .collect(Collectors.toList());
//
//            // Set the filtered veterinarians back to the service
//            service.setVeterinarians(filteredVeterinarians);
//
//            return ResponseEntity.ok(service);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

}
