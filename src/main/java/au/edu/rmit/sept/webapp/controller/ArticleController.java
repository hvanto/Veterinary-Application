package au.edu.rmit.sept.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.service.ArticleService;

import java.util.List;
import java.util.Optional;

@Controller
public class ArticleController {
    
    @Autowired
    private ArticleService articleService;

    @GetMapping("/article/{id}")
    public String getArticleById(@PathVariable("id") Long id, Model model) {
        Optional<Article> article = articleService.getArticleById(id);
        if (article.isPresent()) {
            model.addAttribute("article", article.get());
            return "article"; // Returns the view named "article"
        } else {
            return "404"; // Returns a 404 view if the article is not found
        }
    }

    @GetMapping("/article")
    public String listAllArticles(Model model) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "articleList";
    }
}
