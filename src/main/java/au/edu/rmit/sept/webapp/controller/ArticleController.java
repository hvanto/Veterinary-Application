package au.edu.rmit.sept.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.service.ArticleService;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            return "error"; // Returns a 404 view if the article is not found
        }
    }

    @PostMapping("/article/add")
    public String addArticle(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("published_date") String publishedDateStr,
            @RequestParam("image_url") String imageUrl) {

        // Parse published date string to Date object
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date publishedDate = null;
        try {
            publishedDate = (Date) dateFormat.parse(publishedDateStr);
        } catch (ParseException e) {
            // Handle parsing error (e.g., log, return error message)
            return "error"; // Replace with appropriate error handling
        }

        // Create Article object
        Article article = new Article();
        article.setTitle(title);
        article.setDescription(description);
        article.setAuthor(author);
        article.setPublishedDate(publishedDate);
        article.setImageUrl(imageUrl);

        articleService.saveArticle(article);

        return "redirect:/success"; // Replace with the desired redirect URL
    }

    @DeleteMapping("/article/remove/{id}")
    public String removeArticle(@PathVariable("id") Long id) {
        articleService.deleteArticleById(id);
        return "redirect:/"; // Redirect to the homepage or another desired location
    }
}
