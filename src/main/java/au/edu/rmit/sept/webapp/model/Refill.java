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

    @Column(name = "user_name", nullable = false)
    private String userName;

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

    // Constructors
    public Refill() {}

    public Refill(Prescription prescription, String userName, String userPhone, String address, String creditCardNumber, double cost, Long userId) {
        this.prescription = prescription;
        this.userName = userName;
        this.userPhone = userPhone;
        this.address = address;
        this.creditCardNumber = creditCardNumber;
        this.cost = cost;
        this.fulfilled = false;
        this.userId = userId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
