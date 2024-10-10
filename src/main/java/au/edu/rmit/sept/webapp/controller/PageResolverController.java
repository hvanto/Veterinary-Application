package au.edu.rmit.sept.webapp.controller;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles page routing and resolves the necessary view templates.
 */
@Controller
public class PageResolverController {
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

     @GetMapping("/medical-records")
     public String medicalRecords(HttpServletRequest request, Model model) {
         String requestURL = request.getRequestURL().toString();
         String queryString = request.getQueryString();

         model.addAttribute("content", "medical-records");
         model.addAttribute("url", requestURL);
         model.addAttribute("queryString", queryString);

         return "index";
     }
  
    @GetMapping("/userProfile")
    public String userProfile(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        model.addAttribute("content", "userProfile");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);
        return "index";
    }

     @GetMapping("/prescription")
     public String prescription(HttpServletRequest request, Model model) {
         String requestURL = request.getRequestURL().toString();
         String queryString = request.getQueryString();

         model.addAttribute("content", "prescription");
         model.addAttribute("url", requestURL);
         model.addAttribute("queryString", queryString);

         return "index";
     }

    @GetMapping("/veterinarian-signup")
    public String veterinarianSignup(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        model.addAttribute("content", "veterinarian-signup");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        return "index";
    }

    @GetMapping("/veterinarian-login")
    public String veterinarianLogin(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        model.addAttribute("content", "veterinarian-login");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        return "index";
    }

    @GetMapping("/veterinarian-dashboard")
    public String veterinarianDashboard(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        model.addAttribute("content", "veterinarian-dashboard");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        return "veterinarian-dashboard";
    }

    @GetMapping("/veterinarian-upload-records")
    public String veterinarianUploadRecords(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        model.addAttribute("content", "veterinarian-upload-records");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        return "veterinarian-upload-records";
    }

    @GetMapping("/veterinarian-appointments")
    public String veterinarianAppointments(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        model.addAttribute("content", "veterinarian-appointments");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        return "veterinarian-appointments";
    }

    @GetMapping("/veterinarian-prescription")
    public String veterinarianPrescription(HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        model.addAttribute("content", "veterinarian-prescription");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        return "veterinarian-prescription";
    }
}
