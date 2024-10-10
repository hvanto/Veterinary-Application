package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Notification;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Get all notifications for a user, ordered by creation date.
     * @param user
     * @return 
     */
    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findByUserOrderByCreatedOnDesc(user);
    }

    /**
     * Creates a new notification for a given user with a message.
     * Transactional ensures that the database operations are atomic.
     * @param user
     * @param message
     */
    @Transactional
    public void createNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setRead(false);
        notificationRepository.save(notification);
    }
    
    
    /**
     * Marks a notification as read.
     * @param notificationId
     */
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
