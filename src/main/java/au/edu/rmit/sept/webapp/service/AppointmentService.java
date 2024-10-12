package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.Veterinarian;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.repository.AppointmentRepository;
import au.edu.rmit.sept.webapp.repository.UserRepository;
import au.edu.rmit.sept.webapp.repository.VeterinarianRepository;
import au.edu.rmit.sept.webapp.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final PetRepository petRepository;
    private final NotificationService notificationService; // Added for notifications

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              UserRepository userRepository,
                              VeterinarianRepository veterinarianRepository,
                              PetRepository petRepository,
                              NotificationService notificationService) { // Inject NotificationService
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.veterinarianRepository = veterinarianRepository;
        this.petRepository = petRepository;
        this.notificationService = notificationService;
    }

    public Appointment createAppointment(String day, String year, String startTime, String endTime, Long userId, Long veterinarianId, Long petId, String notes) throws Exception {
        // Convert day, year, startTime, endTime to Date types
        String dateString = day + " " + year;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Date appointmentDate = dateFormat.parse(dateString);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date start = timeFormat.parse(startTime);
        Date end = timeFormat.parse(endTime);

        // Fetch user, veterinarian, and pet from the repositories
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        Veterinarian veterinarian = veterinarianRepository.findById(veterinarianId).orElseThrow(() -> new Exception("Veterinarian not found"));
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new Exception("Pet not found"));

        // Create new appointment object
        Appointment appointment = new Appointment(appointmentDate, start, end, notes, user, veterinarian);
        appointment.setPet(pet);

        // Save the appointment
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Send a notification after the appointment is successfully created
        String confirmationMessage = String.format("Your appointment for %s with Dr. %s is confirmed for %s at %s.",
                pet.getName(), veterinarian.getFirstName() + " " + veterinarian.getLastName(), day + " " + year, startTime);
        notificationService.createNotification(user, confirmationMessage);

        return savedAppointment;
    }

    // Method to find appointments by veterinarian and date
    public List<Appointment> getAppointmentsByVeterinarianAndDay(Long veterinarianId, String day, String year) throws Exception {
        // Convert day and year to Date format
        String dateString = day + " " + year;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Date appointmentDate = dateFormat.parse(dateString);

        // Call the repository to get appointments
        return appointmentRepository.findByVeterinarianIdAndAppointmentDate(veterinarianId, appointmentDate);
    }

    // Method to check if an appointment exists between a veterinarian and a pet
    public boolean existsByVeterinarianAndPet(Veterinarian veterinarian, Pet pet) {
        return appointmentRepository.existsByVeterinarianAndPet(veterinarian, pet);
    }

    public Appointment getAppointmentById(Long appointmentId) throws Exception {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new Exception("Appointment not found with ID: " + appointmentId));
    }

    public List<Appointment> getAppointmentsByUser(Long userId) {
        return appointmentRepository.findAllByUserId(userId);
    }

    public Veterinarian getAppointmentVeterinarian(Long appointmentId) {
        return appointmentRepository.getVeterinarianByAppointmentId(appointmentId);
    }

    public Pet getAppointmentPet(Long appointmentId) {
        return appointmentRepository.getPetByAppointmentId(appointmentId);
    }
}
