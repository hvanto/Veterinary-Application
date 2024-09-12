package au.edu.rmit.sept.webapp.model;

import au.edu.rmit.sept.webapp.enums.Weekday;
import jakarta.persistence.*;

@Entity
@Table(name = "veterinarian_availability")
public class VeterinarianAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Enumerated(EnumType.ORDINAL)
    private Weekday Weekday;

    private Integer SlotDuration;
    private Integer BreakStart;
    private Integer BreakEnd;
    private String StartTime;
    private String EndTime;
}
