package au.edu.rmit.sept.webapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.service.ArticleService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.print.DocFlavor.STRING;

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
    public ResponseEntity<Integer> addArticle(
            @RequestParam("title") String title,
            @RequestParam("link") String link,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("published_date") String publishedDate,
            @RequestParam("image_url") String imageUrl) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;

        try {
            date = dateFormat.parse(publishedDate);
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format", e);
        }

        Article article = new Article();
        article.setTitle(title);
        article.setLink(link);
        article.setAuthor(author);
        article.setPublishedDate(date);

        //TODO: make them optional
        article.setDescription(description);
        article.setImageUrl(imageUrl);

        articleService.saveArticle(article);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/article/remove")
    public ResponseEntity<Integer> removeArticle(
            @RequestParam("id") Long id) {

        articleService.deleteArticleById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

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
        //model.addAttribute("keyword", keyword);

        // Return page not found if article page is empty
        if (articlePage.isEmpty()) {
            model.addAttribute("content", "pageNotFound");
            return "index";
        }

        model.addAttribute("content","articleList");
        return "index";
    }

    @GetMapping("/article/search")
    public String searchArticles(@RequestParam("keyword") String keyword,
                                 @RequestParam(defaultValue = "0") int page,
                                 Model model) {
        // Search articles by keywords
        Page<Article> searchResult = articleService.getSearchResult(keyword, page);

        model.addAttribute("articles", searchResult);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("hasNext", searchResult.hasNext());
        model.addAttribute("hasPrevious", searchResult.hasPrevious());
        model.addAttribute("keyword", keyword);


        if (searchResult.isEmpty()) {
            model.addAttribute("content", "pageNotFound");
            return "index";
        }

        model.addAttribute("content","articleList");
        return "index";
    }
}