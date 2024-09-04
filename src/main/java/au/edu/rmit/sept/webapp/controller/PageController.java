package au.edu.rmit.sept.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("mainContent", "home :: content");
        return "layout";
    }

    @GetMapping("/medical-records")
    public String medicalRecords(Model model) {
        model.addAttribute("mainContent", "medicalRecords :: content");
        return "layout";
    }

    @GetMapping("/prescription-management")
    public String prescriptionManagement(Model model) {
        model.addAttribute("mainContent", "prescriptionManagement :: content");
        return "layout";
    }

    @GetMapping("/account-details")
    public String accountDetails(Model model) {
        model.addAttribute("mainContent", "accountDetails :: content");
        return "layout";
    }

    @GetMapping("/book-appointment")
    public String bookAppointment(Model model) {
        model.addAttribute("mainContent", "bookAppointment :: content");
        return "layout";
    }

    @GetMapping("/manage-appointment")
    public String manageAppointment(Model model) {
        model.addAttribute("mainContent", "manageAppointment :: content");
        return "layout";
    }

    @GetMapping("/order-tracking")
    public String orderTracking(Model model) {
        model.addAttribute("mainContent", "orderTracking :: content");
        return "layout";
    }
}

