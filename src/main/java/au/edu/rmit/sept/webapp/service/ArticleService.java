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

    public List<Article> getAllArticles() {
        return repository.findAll();
    }

    // Check if an article exists by link
    public boolean articleExists(String link) {
        return repository.findByLink(link).isPresent();
    }

    // Pagination
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

    // Fetch RSS feed from external URL and save to database
    public void fetchRssFeed() {
        // Fetch RSS feed only once
        if (!isFetched) {
            try {
                // Delete old RSS feed from the database
                repository.deleteAll();

                String testLink = "https://www.petmd.com/feed";
                URL url = new URL(testLink);
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(url));

                List<Article> articles = new ArrayList<>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                for (SyndEntry entry : feed.getEntries()) {
                    Article article = getArticle(entry);
                    repository.save(article);
                }
                isFetched = true;

            } catch (MalformedURLException e) {
                System.err.println("The URL is malformed: " + e.getMessage());

            } catch (FeedException e) {
                System.err.println("Error parsing the RSS feed: " + e.getMessage());

            } catch (IOException e) {
                System.err.println("Error fetching the RSS feed: " + e.getMessage());

            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static Article getArticle(SyndEntry entry) {
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

        return article;
    }

    public static void writeZipToStream(String url, ZipOutputStream zos) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Set<String> downloaded = new HashSet<>();

        for (Element link : doc.select("link[rel=stylesheet]")) {
            String absUrl = link.absUrl("href");

            if (!absUrl.startsWith("data:")) {
                String path = "css/" + getFileName(absUrl);

                if (!downloaded.contains(absUrl)) {
                    addFileToZip(path, downloadFile(absUrl), zos);
                    downloaded.add(absUrl);
                }
                link.attr("href", path);
            }
        }

        for (Element img : doc.select("img")) {
            String absUrl = img.absUrl("src");

            if (!absUrl.startsWith("data:")) {
                String path = "img/" + getFileName(absUrl);

                if (!downloaded.contains(absUrl)) {
                    addFileToZip(path, downloadFile(absUrl), zos);
                    downloaded.add(absUrl);
                }
                img.attr("src", path);
            }
        }
        addFileToZip("article.html", doc.outerHtml().getBytes(), zos);
    }

    private static String getFileName(String url) {
        int i = url.lastIndexOf("?");
        return url.substring(url.lastIndexOf("/") + 1, (i < 0) ? url.length() : i);
    }

    private static void addFileToZip(String path, byte[] bytes, ZipOutputStream zos) {
        try {
            ZipEntry entry = new ZipEntry(path);
            zos.putNextEntry(entry);
            zos.write(bytes);
            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] downloadFile(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        try (InputStream in = url.openStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }
}
