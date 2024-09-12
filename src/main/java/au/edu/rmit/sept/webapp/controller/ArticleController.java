package au.edu.rmit.sept.webapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.service.ArticleService;
import org.springframework.web.bind.annotation.RequestParam;

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
            return "pageNotFound"; // Returns a 404 view if the article is not found
        }
    }
/*
    @GetMapping("/article")
    public String getArticlesPage(@RequestParam(defaultValue = "0") int page, Model model) {
        // Fetch RSS feed only once
        articleService.fetchRssFeed();

        // Get articles from database
        Page<Article> articlePage = articleService.getArticles(page);

        model.addAttribute("articles", articlePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("hasNext", articlePage.hasNext());
        model.addAttribute("hasPrevious", articlePage.hasPrevious());

        // Return page not found if article page is empty
        if (articlePage.isEmpty()) {
            return "pageNotFound";
        }

        return "articleList";
    }
*/
    @GetMapping("/article")
    public String getArticlesPage(@RequestParam(defaultValue = "0") int page, HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        // Fetch RSS feed only once
        articleService.fetchRssFeed();

        // Get articles from database
        Page<Article> articlePage = articleService.getArticles(page);

        model.addAttribute("articles", articlePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("hasNext", articlePage.hasNext());
        model.addAttribute("hasPrevious", articlePage.hasPrevious());

        // Return page not found if article page is empty
        if (articlePage.isEmpty()) {
            model.addAttribute("content", "pageNotFound");
            return "index";
        }

        model.addAttribute("content","articleList");
        return "index";
    }


}
