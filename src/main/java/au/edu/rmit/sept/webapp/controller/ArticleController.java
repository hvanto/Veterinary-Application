package au.edu.rmit.sept.webapp.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.model.Bookmark;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.ArticleService;
import au.edu.rmit.sept.webapp.service.BookmarkService;
import au.edu.rmit.sept.webapp.service.UserService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

/**
 * Controller for managing articles and bookmarks.
 * This controller provides methods to handle requests related to articles and bookmarks, including
 * viewing, downloading, adding, and removing articles and bookmarks.
 */
@Controller
public class ArticleController {

    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private BookmarkService bookmarkService;

    /**
     * Handles GET requests for retrieving an article by its ID.
     * 
     * @param id    The ID of the article to retrieve.
     * @param model The model to pass data to the view.
     * @return The view name ("article") if the article exists, or "error" if not.
     */
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

    /**
     * Handles GET requests for downloading an article's contents as a zip file.
     * 
     * @param link     The URL of the article to download.
     * @param response The HTTP response to send the zip file.
     * @return A StreamingResponseBody that streams the zip file to the client.
     * @throws Exception If an error occurs during zipping or downloading.
     */
    @GetMapping("/downloadArticle")
    public ResponseEntity<StreamingResponseBody> downloadArticle(@RequestParam String link,
            HttpServletResponse response)
            throws Exception {

        response.setContentType("application/zip");
        response.setHeader(
                "Content-Disposition",
                "attachment;filename=download.zip");

        StreamingResponseBody stream = out -> {
            try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
                ArticleService.writeZipToStream(link, zos);
            }
        };

        return ResponseEntity.ok().body(stream);
    }

    /**
     * Handles GET requests for retrieving the bookmarks of a user.
     * 
     * @param userId The ID of the user whose bookmarks are retrieved.
     * @return A ResponseEntity containing a set of article links bookmarked by the user.
     */
    @GetMapping("/getBookmarks")
    public ResponseEntity<Set<String>> getBookmarks(@RequestParam Long userId) {
        User user = userService.findById(userId).get();

        Set<String> bookmarks = bookmarkService.findByUser(user).stream() // this line
                .map(bookmark -> bookmark.getArticle().getLink())
                .collect(Collectors.toSet());

        return ResponseEntity.ok().body(bookmarks);
    }

    /**
     * Handles POST requests to add a new article.
     * 
     * @param article The article to add.
     * @return A ResponseEntity containing the ID of the added article.
     */
    @PostMapping("/article/add")
    public ResponseEntity<Long> addArticle(@RequestBody Article article) {
        Long id = articleService.saveArticle(article).getId();
        return ResponseEntity.ok(id);
    }

    /**
     * Handles DELETE requests to remove an article by its ID.
     * 
     * @param id The ID of the article to remove.
     * @return A ResponseEntity indicating success.
     */
    @DeleteMapping("/article/remove")
    public ResponseEntity<Void> removeArticle(@RequestParam("id") Long id) {
        articleService.deleteArticleById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Handles GET requests for retrieving paginated articles.
     * 
     * @param page  The page number of articles to retrieve (defaults to 0).
     * @param model The model to pass data to the view.
     * @return The view name ("index") for displaying articles.
     */
    @GetMapping("/article")
    public String getArticlesPage(@RequestParam(defaultValue = "0") int page, Model model) {
        // Fetch RSS feed only once
        articleService.fetchRssFeed();

        // Get articles from database
        Page<Article> articlePage = articleService.getArticles(page);

        model.addAttribute("mode", "default");
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

        model.addAttribute("content", "articleList");
        return "index";
    }

    /**
     * Handles GET requests for searching articles based on a keyword.
     * 
     * @param keyword The keyword to search for in the articles.
     * @param page    The page number of search results (defaults to 0).
     * @param model   The model to pass data to the view.
     * @return The view name ("index") for displaying the search results.
     */
    @GetMapping("/article/search")
    public String searchArticles(@RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        // Search articles by keywords
        Page<Article> searchResult = articleService.getSearchResult(keyword, page);

        model.addAttribute("mode", "search");
        model.addAttribute("articles", searchResult);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("hasNext", searchResult.hasNext());
        model.addAttribute("hasPrevious", searchResult.hasPrevious());
        model.addAttribute("keyword", keyword);
        model.addAttribute("isEmpty", searchResult.isEmpty());

        model.addAttribute("content", "articleList");
        return "index";
    }

    /**
     * Handles GET requests for retrieving paginated bookmarks of a user.
     * 
     * @param page   The page number of bookmarked articles to retrieve (defaults to 0).
     * @param userId The ID of the user whose bookmarks are retrieved.
     * @param model  The model to pass data to the view.
     * @return The view name ("index") for displaying bookmarks.
     */
    @GetMapping("/bookmark")
    public String getBookmarks(@RequestParam(defaultValue = "0") int page,
            @RequestParam Long userId, Model model) {
        User user = userService.findById(userId).get();

        // Fetch paginated bookmarked articles
        Page<Article> articlePage = bookmarkService.findByUser(user, page).map(Bookmark::getArticle);

        // Prepare the set of bookmarked article links (for display purposes)
        Set<String> bookmarks = articlePage.getContent().stream()
                .map(Article::getLink)
                .collect(Collectors.toSet());

        // Add attributes to the model
        model.addAttribute("mode", "bookmark");
        model.addAttribute("articles", articlePage);
        model.addAttribute("bookmarks", bookmarks);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("hasNext", articlePage.hasNext());
        model.addAttribute("hasPrevious", articlePage.hasPrevious());
        model.addAttribute("isEmpty", articlePage.isEmpty());

        model.addAttribute("content", "articleList");
        return "index";
    }

    /**
     * Handles POST requests to add a bookmark for an article.
     * 
     * @param requestArticle The article to be bookmarked.
     * @param userId         The ID of the user adding the bookmark.
     * @return A ResponseEntity containing the ID of the added bookmark.
     */
    @PostMapping("/api/bookmark/add")
    public ResponseEntity<Long> addBookmark(@RequestBody Article requestArticle, @RequestParam Long userId) {
        Optional<Article> existingArticle = articleService.getArticleByLink(requestArticle.getLink());
        Article article = null;

        if (existingArticle.isPresent()) {
            // Use existing article if already in database
            article = existingArticle.get();
        } else {
            // Add article to db if does not already present
            article = articleService.saveArticle(requestArticle);
        }

        User user = userService.findById(userId).get();
        Bookmark bookmark = bookmarkService.addBookmark(user, article);

        return ResponseEntity.ok(bookmark.getId());
    }

    /**
     * Handles POST requests to remove a bookmark for an article.
     * 
     * @param requestArticle The article to remove from bookmarks.
     * @param userId         The ID of the user removing the bookmark.
     * @return A ResponseEntity indicating success.
     */
    @PostMapping("/api/bookmark/remove")
    public ResponseEntity<Void> removeBookmark(@RequestBody Article requestArticle, @RequestParam Long userId) {
        Optional<Article> existingArticle = articleService.getArticleByLink(requestArticle.getLink());
        Article article = null;

        article = existingArticle.get();

        User user = userService.findById(userId).get();
        bookmarkService.removeBookmark(user, article);

        return ResponseEntity.ok().build();
    }
}