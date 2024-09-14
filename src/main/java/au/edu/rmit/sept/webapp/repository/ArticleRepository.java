package au.edu.rmit.sept.webapp.repository;


import au.edu.rmit.sept.webapp.model.Article;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByLink(String link);
}
