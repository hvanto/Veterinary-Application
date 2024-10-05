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
            Clinic defaultClinic = new Clinic("Independent", "independent@clinic.com", "N/A", "N/A", 0, 24, 0L);
            clinicService.saveClinic(defaultClinic);
        }
    }
}
