package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.repository.ArticleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository repository;

    public Optional<Article> getArticleById(Long id) {
        return repository.findById(id);
    }

    public List<Article> getAllArticles() {
        return repository.findAll();
    }
}
