package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.repository.ArticleRepository;

import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository repository;

    public Optional<Article> getArticleById(Long id) {
        return repository.findById(id);
    }

    public Optional<Article> getArticleByLink(String link) {
        return repository.findByLink(link);
    }

    public Article saveArticle(Article article) {
        return repository.save(article);
    }

    public void deleteArticleById(Long id) {
        repository.deleteById(id);
    }

    public List<Article> getAllArticles() {
        return repository.findAll();
    }

    // Check if an article exists by link
    public boolean articleExists(String link) {
        return repository.findByLink(link).isPresent();
    }

    // Pagination
    public Page<Article> getArticles(int page) {
        // Show 10 articles per page
        Pageable pageable = PageRequest.of(page, 10);
        return repository.findAll(pageable);
    }

    // Fetch RSS feed from external URL and save to database
    public void fetchRssFeed() throws Exception {
        // Delete old RSS feed from the database
        repository.deleteAll();

        String testLink = "https://www.petmd.com/feed";
        URL url = new URL(testLink);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(url));

        List<Article> articles = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (SyndEntry entry : feed.getEntries()) {
            Article article = new Article();
            article.setTitle(entry.getTitle());
            article.setLink(entry.getLink());
            article.setAuthor(entry.getAuthor());
            article.setPublishedDate(entry.getPublishedDate());

            // Set description if not null
            if (entry.getDescription() != null && !entry.getDescription().getValue().isEmpty()) {
                article.setDescription(entry.getDescription().getValue());
            }

            // Get Image from enclosure
            List<SyndEnclosure> enclosures = entry.getEnclosures();
            if (!enclosures.isEmpty()) {
                article.setImageUrl(enclosures.get(0).getUrl());
            } else {
                article.setImageUrl("No image available");
            }

            repository.save(article);
        }
    }
}
