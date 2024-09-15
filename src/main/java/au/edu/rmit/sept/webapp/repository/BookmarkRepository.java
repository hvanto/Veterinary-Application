package au.edu.rmit.sept.webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.model.Bookmark;
import au.edu.rmit.sept.webapp.model.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserAndArticle(User user, Article article);
    List<Bookmark> findByUser(User user);
    Page<Bookmark> findByUser(User user, Pageable pageable);
}
