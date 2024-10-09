package au.edu.rmit.sept.webapp.repository;


import au.edu.rmit.sept.webapp.model.Article;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing {@link Article} entities.
 * Extends {@link JpaRepository} to provide CRUD operations and custom queries for articles.
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * Retrieves an optional article by its link.
     *
     * @param link The URL link of the article to be retrieved.
     * @return An {@link Optional} containing the article if found, or empty if not found.
     */
    Optional<Article> findByLink(String link);

    /**
     * Searches for articles by a keyword in the title, description, or author fields.
     * The search is case-insensitive.
     *
     * @param keyword  The keyword to search for in the article title, description, or author.
     * @param pageable Pagination information (page number, size, sort).
     * @return A paginated list of articles matching the search criteria.
     */
    @Query(value = "SELECT * FROM articles " +
            "WHERE LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(author) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            nativeQuery = true)
    Page<Article> searchArticlesByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
