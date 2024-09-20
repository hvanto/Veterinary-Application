package au.edu.rmit.sept.webapp.model;

import au.edu.rmit.sept.webapp.enums.Weekday;
import jakarta.persistence.*;

@Entity
@Table(name = "veterinarian_availability")
public class VeterinarianAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    private Veterinarian veterinarian;

    @Enumerated(EnumType.ORDINAL)
    private Weekday Weekday;

    private Integer SlotDuration;
    private Integer BreakStart;
    private Integer BreakEnd;
    private String StartTime;
    private String EndTime;

    // Constructors
    public VeterinarianAvailability() {
    }

    public VeterinarianAvailability(Veterinarian veterinarian, Weekday weekday, Integer slotDuration, Integer breakStart, Integer breakEnd, String startTime, String endTime) {
        this.veterinarian = veterinarian;
        Weekday = weekday;
        SlotDuration = slotDuration;
        BreakStart = breakStart;
        BreakEnd = breakEnd;
        StartTime = startTime;
        EndTime = endTime;
    }
    // Getters and Setters

    public Long getId() {
        return Id;
    }

    public au.edu.rmit.sept.webapp.enums.Weekday getWeekday() {
        return Weekday;
    }

    public void setWeekday(au.edu.rmit.sept.webapp.enums.Weekday weekday) {
        Weekday = weekday;
    }

    public Integer getSlotDuration() {
        return SlotDuration;
    }

    public void setSlotDuration(Integer slotDuration) {
        SlotDuration = slotDuration;
    }

    public Integer getBreakStart() {
        return BreakStart;
    }

    public void setBreakStart(Integer breakStart) {
        BreakStart = breakStart;
    }

    public Integer getBreakEnd() {
        return BreakEnd;
    }

    public void setBreakEnd(Integer breakEnd) {
        BreakEnd = breakEnd;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
}
