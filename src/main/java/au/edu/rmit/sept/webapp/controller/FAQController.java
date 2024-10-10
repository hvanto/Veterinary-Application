package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.FAQ;
import au.edu.rmit.sept.webapp.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@Controller
public class FAQController {

    @Autowired
    private FAQRepository faqRepository;

    @GetMapping("/faq/{category}")
    public String getFaqById(@PathVariable("category") String category, Model model) {
        try {
            List<FAQ> faqs = faqRepository.findByCategory(category);

            model.addAttribute("faqs", faqs);
            model.addAttribute("isEmpty", faqs.isEmpty());

        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while retrieving FAQs.");
            model.addAttribute("faqs", Collections.emptyList());
            model.addAttribute("isEmpty", true);
        }

        model.addAttribute("category", category);
        model.addAttribute("content", "faq");
        return "index";
    }

}
