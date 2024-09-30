package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Notification;
import au.edu.rmit.sept.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedOnDesc(User user);
}
