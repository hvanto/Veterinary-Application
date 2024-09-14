package au.edu.rmit.sept.webapp.repository;


import au.edu.rmit.sept.webapp.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = "SELECT * FROM articles " +
            "WHERE LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(author) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            nativeQuery = true)
    List<Article> searchArticlesByKeyword(@Param("keyword") String keyword);
}
