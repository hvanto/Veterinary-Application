package au.edu.rmit.sept.webapp.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.server.ResponseStatusException;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.model.Bookmark;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.ArticleService;
import au.edu.rmit.sept.webapp.service.BookmarkService;
import au.edu.rmit.sept.webapp.service.UserService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ArticleController {

    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private BookmarkService bookmarkService;

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

    @PostMapping("/downloadArticle")
    public ResponseEntity<InputStreamResource> downloadArticle(@RequestBody String link) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Create pdf of webpage from link
        try {
            // Fetch the webpage content
            String decodedLink = URLDecoder.decode(link, StandardCharsets.UTF_8.name());
            String html = Jsoup.connect(link).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3").get().html();

            System.out.println(html);

            // Create PDF of webpage from the HTML content
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            
            PdfWriter writer = new PdfWriter(pdfOutputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add the webpage content to the PDF
            document.add(new Paragraph("Webpage content from: " + link)); // Optional header
            document.add(new Paragraph(Jsoup.parse(html).text())); // Add the extracted text from HTML
            document.close();

            // Write PDF to byte array output stream
            byteArrayOutputStream.write(pdfOutputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace(); // Log the error and handle the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Prepare the response
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        InputStreamResource resource = new InputStreamResource(inputStream);

        String filename = "output.pdf";
        filename = filename.replaceAll("_+", "_").replaceAll("^_|_$", ""); // Further sanitize

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayOutputStream.size())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
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

        // TODO: make them optional
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
    public String getArticlesPage(@RequestParam(defaultValue = "0") int page, HttpServletRequest request, Model model) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        model.addAttribute("url", requestURL);
        model.addAttribute("queryString", queryString);

        // TODO: retrieve user by userId from cookie
        User user = userService.findFirst().get();
        Set<String> bookmarks = bookmarkService.findByUser(user).stream() // this line
                .map(bookmark -> bookmark.getArticle().getLink())
                .collect(Collectors.toSet());

        // Fetch RSS feed only once
        articleService.fetchRssFeed();

        // Get articles from database
        Page<Article> articlePage = articleService.getArticles(page);

        model.addAttribute("articles", articlePage);
        model.addAttribute("bookmarks", bookmarks);
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

    @GetMapping("/bookmark")
    public String getBookmarks(@RequestParam(defaultValue = "0") int page, Model model) {
        // TODO: Retrieve the user from the session or authentication context
        User user = userService.findFirst().get();

        // Fetch paginated bookmarked articles
        Page<Article> articlePage = bookmarkService.findByUser(user, page).map(Bookmark::getArticle);

        // Prepare the set of bookmarked article links (for display purposes)
        Set<String> bookmarks = articlePage.getContent().stream()
                .map(Article::getLink)
                .collect(Collectors.toSet());

        // Add attributes to the model
        model.addAttribute("articles", articlePage);
        model.addAttribute("bookmarks", bookmarks);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("hasNext", articlePage.hasNext());
        model.addAttribute("hasPrevious", articlePage.hasPrevious());

        return "articleList";
    }

    @PostMapping("/addBookmark")
    public ResponseEntity<String> addBookmark(@RequestBody Article requestArticle) {
        Optional<Article> existingArticle = articleService.getArticleByLink(requestArticle.getLink());
        Article article = null;

        if (existingArticle.isPresent()) {
            article = existingArticle.get();
        } else {
            // Add article to db if does not already present
            article = articleService.saveArticle(article);
        }

        try {
            // TODO: retrieve user by userId from cookie
            User user = userService.findFirst().get();
            bookmarkService.addBookmark(user, article);
            return ResponseEntity.ok("Bookmark added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/removeBookmark")
    public ResponseEntity<String> removeBookmark(@RequestBody Article requestArticle) {
        Optional<Article> existingArticle = articleService.getArticleByLink(requestArticle.getLink());
        Article article = null;

        try {
            article = existingArticle.get();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        try {
            // TODO: retrieve user by userId from cookie
            User user = userService.findFirst().get();
            bookmarkService.removeBookmark(user, article);
            return ResponseEntity.ok("Bookmark added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}