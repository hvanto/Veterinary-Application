package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {
    // Find the faq by category
    List<FAQ> findByCategory(String category);
}

