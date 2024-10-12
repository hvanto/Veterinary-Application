package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "refill")
public class Refill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "credit_card_number", nullable = false)
    private String creditCardNumber;

    @Column(name = "cost", nullable = false)
    private double cost;

    @Column(name = "fulfilled", nullable = false)
    private boolean fulfilled = false;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "cvv", nullable = false)
    private Long cvv;

    @Column(name = "expiry_date", nullable = false)
    private String expiryDate;

    @Column(name = "submission_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP) // Use this for timestamp
    private Date submissionDate; // New field for submission date

    @Column(name = "order_tracking", nullable = false)
    private String tracking;


    // Constructors
    public Refill() {}

    public Refill(Prescription prescription, String firstName, String lastName, String userPhone, String address, String creditCardNumber, double cost, Long userId, String expiryDate, Long cvv, Date submissionDate, String tracking) {
        this.prescription = prescription;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userPhone = userPhone;
        this.address = address;
        this.creditCardNumber = creditCardNumber;
        this.cvv = cvv;
        this.cost = cost;
        this.fulfilled = false;
        this.userId = userId;
        this.expiryDate = expiryDate;
        this.submissionDate = submissionDate;
        this.tracking = tracking;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getCvv() {
        return cvv;
    }

    public void setCvv(Long cvv) {
        this.cvv = cvv;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getTracking() {return tracking;}

    public void setTracking(String tracking) {this.tracking = tracking;}
}
