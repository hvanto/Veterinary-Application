package au.edu.rmit.sept.webapp.config;

import au.edu.rmit.sept.webapp.model.Clinic;
import au.edu.rmit.sept.webapp.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class StartupDataLoader {

    private final ClinicService clinicService;

    @Autowired
    public StartupDataLoader(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PostConstruct
    public void createDefaultClinic() {
        // Check if the "Independent" clinic exists, if not, create it
        if (!clinicService.clinicExists("Independent")) {
            Clinic clinic = new Clinic("Independent", "independent@vetclinic.com", "123 Main St", "City", 900, 1700, 1234567890L);
            clinicService.saveClinic(clinic);
        }
    }
}
