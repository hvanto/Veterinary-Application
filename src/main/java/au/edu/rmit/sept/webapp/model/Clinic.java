package au.edu.rmit.sept.webapp.model;

import au.edu.rmit.sept.webapp.enums.Weekday;
import jakarta.persistence.*;

@Entity
@Table(name = "clinic")
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String Name;
    private String Email;
    private String Address;
    private String location;
    private Integer OpeningTime;
    private Integer ClosingTime;
    private Integer Contact;
}
