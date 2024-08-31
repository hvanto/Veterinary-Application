package au.edu.rmit.sept.webapp.repository;


import au.edu.rmit.sept.webapp.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
