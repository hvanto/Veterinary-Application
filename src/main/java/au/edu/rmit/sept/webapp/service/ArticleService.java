package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.repository.ArticleRepository;

import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository repository;

    private boolean isFetched = false;

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

    private List<Article> getAllArticles() {
        return repository.findAll();
    }

    // Check if an article exists by link
    private boolean articleExists(String link) {
        return repository.findByLink(link).isPresent();
    }

    // Pagination for all articles
    public Page<Article> getArticles(int page) {
        try {
            if (page < 0) {
                throw new IllegalArgumentException("page cannot be negative");
            }
            // Show 10 articles per page
            Pageable pageable = PageRequest.of(page, 10);
            return repository.findAll(pageable);

        } catch (Exception e) {
            System.err.println("An error occurred while fetching paginated articles: " + e.getMessage());
            // Return an empty page
            return Page.empty();
        }

    }

    // Pagination for search results
    public Page<Article> getSearchResult(String keyword, int page) {
        try {
            if (page < 0) {
                throw new IllegalArgumentException("page cannot be negative");
            }
            // Show 10 articles per page
            Pageable pageable = PageRequest.of(page, 10);
            return repository.searchArticlesByKeyword(keyword, pageable);

        } catch (Exception e) {
            System.err.println("An error occurred while fetching paginated articles: " + e.getMessage());
            // Return an empty page
            return Page.empty();
        }

    }

    // Get articles from Rss feed link
    private List<Article> getRssArticles(String link) {
        List<Article> articles = new ArrayList<>();
        try {
            URL url = new URL(link);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            // Serialize feed entries to articles
            for (SyndEntry entry : feed.getEntries()) {
                articles.add(getArticle(entry));
            }
        } catch (MalformedURLException e) {
            System.err.println("RSS feed URL is malformed: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error fetching the RSS feed: " + e.getMessage());
        } catch (FeedException e) {
            System.err.println("Error parsing the RSS feed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
        return articles;
    }

    // Fetch RSS feed from external URL to repository
    public void fetchRssFeed() {
        String test_link = "https://www.petmd.com/feed";
        // Get articles from RSS feed
        for (Article article : getRssArticles(test_link)) {
            if (!articleExists(article.getLink())) {
                repository.save(article);
            }
        }
    }

    // Static methods

    private static Article getArticle(SyndEntry entry) {
        Article article = new Article();

        article.setTitle(entry.getTitle());
        article.setLink(entry.getLink());
        article.setAuthor(entry.getAuthor());
        article.setPublishedDate(entry.getPublishedDate());

        // Set description if not null
        if (entry.getDescription() != null
                && !entry.getDescription().getValue().isEmpty()) {
            article.setDescription(entry.getDescription().getValue());
        }

        // Get Image from enclosure
        List<SyndEnclosure> enclosures = entry.getEnclosures();
        if (!enclosures.isEmpty()) {
            article.setImageUrl(enclosures.get(0).getUrl());
        } else {
            article.setImageUrl("No image available");
        }
        return article;
    }

    private static void addElementsToDirectory(Elements elements, String attribute, String directory,
            ZipOutputStream zos) {
        // Storing download file name set to avoid repeats
        Set<String> downloaded = new HashSet<>();

        for (Element element : elements) {
            String absUrl = element.absUrl(attribute);

            // Skip if the URL starts with 'data:' as it is likely an embedded resource.
            if (!absUrl.startsWith("data:")) {
                String path = directory + "/" + getFileName(absUrl);

                // Check if the URL has already been downloaded to avoid duplicates.
                if (!downloaded.contains(absUrl)) {
                    // Download the file and add it to the zip.
                    addToZip(path, downloadFile(absUrl), zos);
                    downloaded.add(absUrl);
                }
                // Update the element's attribute (href/src) to the new path.
                element.attr(attribute, path);
            }
        }
    }

    public static void writeZipToStream(String url, ZipOutputStream zos) {
        try {
            Document doc = Jsoup.connect(url).get();

            addElementsToDirectory(doc.select("link[rel=stylesheet]"), "href", "css", zos);
            addElementsToDirectory(doc.select("img"), "src", "img", zos);

            addToZip("article.html", doc.outerHtml().getBytes(), zos);
        } catch (IOException e) {

        }
    }

    private static String getFileName(String url) {
        int i = url.lastIndexOf("?");
        return url.substring(url.lastIndexOf("/") + 1, (i < 0) ? url.length() : i);
    }

    private static void addToZip(String path, byte[] bytes, ZipOutputStream zos) {
        try {
            ZipEntry entry = new ZipEntry(path);
            zos.putNextEntry(entry);
            zos.write(bytes);
            zos.closeEntry();
        } catch (IOException e) {
            System.err.println("Error writing bytes to zip output stream: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static byte[] downloadFile(String fileUrl) {
        try {
            URL url = new URL(fileUrl);

            try (InputStream in = url.openStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                return baos.toByteArray();
            }
        } catch (MalformedURLException e) {
            System.err.println("Download URL is malformed: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error downloading: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
        // Fallback to return empty file
        return new byte[0];
    }
}
