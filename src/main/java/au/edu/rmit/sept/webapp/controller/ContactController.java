package au.edu.rmit.sept.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public String contactPage(Model model) {
        model.addAttribute("content", "contact");
        return "index";
    }

    @PostMapping("/contact/send-email")
    public ResponseEntity<String> sendMail(@RequestBody Map<String, String> requestBody) {
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");
        String email = requestBody.get("email");
        String message = requestBody.get("message");

        try {
            // Send email to support team
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
