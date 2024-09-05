package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.model.ArticleRss;
import au.edu.rmit.sept.webapp.repository.ArticleRepository;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
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

    public List<Article> getAllArticles() {
        return repository.findAll();
    }

    // Fetch and parse RSS feed from an external URL
    public List<ArticleRss> fetchRssFeed() throws Exception {
        String testLink = "https://feeds.feedburner.com/gopetfriendly";
        URL url = new URL(testLink);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(url));

        List<ArticleRss> articles = new ArrayList<>();

        for (SyndEntry entry : feed.getEntries()) {
            ArticleRss article = new ArticleRss();
            article.setTitle(entry.getTitle());
            article.setLink(entry.getLink());

            articles.add(article);
        }

        return articles;
    }
}
