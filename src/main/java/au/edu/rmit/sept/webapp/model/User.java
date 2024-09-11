package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String first_name;
    private String last_name;

    @Column(unique = true)
    private String email;
    private String contact;
    private String image;

    private String password;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_on;

    private boolean deleted;

    @PrePersist
    protected void onCreate() {
        image = "default_profile.png";
        created_on = new Date();
        deleted = false;
    }
}
