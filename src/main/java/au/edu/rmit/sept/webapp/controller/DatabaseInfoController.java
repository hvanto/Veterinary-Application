package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.DatabaseInfo;
import au.edu.rmit.sept.webapp.service.DatabaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class DatabaseInfoController {

    @Autowired
    private DatabaseInfoService service;

    @GetMapping("/db-info")
    public String getDatabaseInfo(Model model) {
        DatabaseInfo info = service.saveDatabaseInfo("Connection successful");
        model.addAttribute("dbInfo", info);
        return "db-info";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Model model, Exception ex) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}
