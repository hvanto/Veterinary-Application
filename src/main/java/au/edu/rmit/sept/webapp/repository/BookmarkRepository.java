package au.edu.rmit.sept.webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.model.Bookmark;
import au.edu.rmit.sept.webapp.model.User;

/**
 * Repository interface for managing {@link Bookmark} entities.
 * Extends {@link JpaRepository} to provide CRUD operations and custom queries for bookmarks.
 */
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    
    /**
     * Retrieves an optional bookmark for a specific user and article.
     *
     * @param user    The user associated with the bookmark.
     * @param article The article associated with the bookmark.
     * @return An {@link Optional} containing the bookmark if found, or empty if not found.
     */
    Optional<Bookmark> findByUserAndArticle(User user, Article article);

    /**
     * Retrieves a list of all bookmarks for a specific user.
     *
     * @param user The user whose bookmarks are to be retrieved.
     * @return A list of bookmarks associated with the given user.
     */
    List<Bookmark> findByUser(User user);

    /**
     * Retrieves a paginated list of bookmarks for a specific user.
     *
     * @param user     The user whose bookmarks are to be retrieved.
     * @param pageable Pagination information (page number, size, sort).
     * @return A paginated list of bookmarks for the user.
     */
    Page<Bookmark> findByUser(User user, Pageable pageable);
}
