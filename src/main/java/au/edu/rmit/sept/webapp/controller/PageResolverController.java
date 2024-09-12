package au.edu.rmit.sept.webapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author ashmit-sachan
 * @version 1.0
 */
@Controller
public class PageResolverController {
    /**
     * Handles the request to the home page and provides necessary data to the view.
     *
     * @param request   - The HTTP request containing the URL and query data
     * @param model     - Data object used by Thymeleaf for rendering the view
     * @return index.html page with "home" content as the main section
     *
     * @implNote This method is intended to return the home page of the application
     *           and may be modified in the future to handle additional logic or data.
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
     *
     * @implNote This method is intended to return the home page of the application
     *           and may be modified in the future to handle additional logic or data.
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


    /**
     * Handles the request to the home page and provides necessary data to the view.
     *
     * @param request   - The HTTP request containing the URL and query data
     * @param model     - Data object used by Thymeleaf for rendering the view
     * @return index.html page with "home" content as the main section
     *
     * @implNote This method is intended to return the home page of the application
     *           and may be modified in the future to handle additional logic or data.
     */
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

        model.addAttribute("content", "login");
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        return "index";
    }


    /**
     * Returns the correct page based on URL as a fallback
     *
     * @param request   - The HTTP request containing query data
     * @param page      - Path Variable responsible for returning the correct page
     * @param model     - Data Object for ThymeLeaf Template
     * @return index.html page with an appropriate main content area
     *
     * @implNote 1. This method will be modified in the future to
     *           accommodate any changes in the URL structure
     *           2. Does not resolve JS and CSS (issue)
     */
//    @GetMapping("/{page}/**")
//    public String pageResolver(HttpServletRequest request, @PathVariable String page, Model model) {
//        model.addAttribute("content", page);
//        return "index";
//    }
}
