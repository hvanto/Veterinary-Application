package au.edu.rmit.sept.webapp;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.repository.ArticleRepository;
import au.edu.rmit.sept.webapp.repository.BookmarkRepository;
import au.edu.rmit.sept.webapp.service.ArticleService;

@SpringBootTest
public class ArticleServiceTests {
    @Autowired
    private ArticleService service;
    @Autowired
    private ArticleRepository repository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    
    @BeforeEach
    public void setUp() {
        bookmarkRepository.deleteAll();
        // Delete all articles from database
        repository.deleteAll();

        // Add custom test data
        Article article1 = new Article("Lorem Ipsum", "https://somelink.com",
                "Dolor amet", "Nullam Volutpat",
                new Date(), "https://imagelink.com");
        Article article2 = new Article("Curabitur non justo", "https://somelink.com",
                "Congue tristique", "Donec Maximus",
                new Date(), "https://imagelink.com");

        repository.save(article1);
        repository.save(article2);
    }

    @Test
    public void testGetRssArticles_success() {
        List<Article> articles = service.getRssArticles("https://www.petmd.com/feed");
        // More than 0 articles are returned
        assertNotEquals(articles.size(), 0);
    }

    @Test
    public void testGetRssArticles_malformedUrl() {
        // A malformed URL that cannot be parsed correctly
        String malformedUrl = "not a url ()()()";

        List<Article> articles = service.getRssArticles(malformedUrl);

        // Expect the service to return an empty list
        assertTrue(articles.isEmpty(), "Expected an empty list for a malformed URL");
    }

    @Test
    public void testGetRssArticles_iOError() {
        // Pass a URL that simulates being down or unreachable
        String downUrl = "http://example.invalid";
    
        // Call the service and check that it returns an empty list
        List<Article> articles = service.getRssArticles(downUrl);
    
        // Validate that no articles are returned
        assertTrue(articles.isEmpty(), "Expected an empty list for an unreachable URL");
    }

    @Test
    public void testGetRssArticles_rssFeedError() {
        // Pass a valid URL that points to a non-RSS feed
        String nonRssFeedUrl = "https://www.google.com";
    
        // Call the service and check that it returns an empty list
        List<Article> articles = service.getRssArticles(nonRssFeedUrl);
    
        // Validate that no articles are returned
        assertTrue(articles.isEmpty(), "Expected an empty list for a non-RSS feed URL");
    }

    @Test
    public void testGetArticles_success() {
        Page<Article> articles = service.getArticles(0);
        // More than 0 articles are returned
        assertNotEquals(articles.getSize(), 0);
    }

    @Test
    public void testGetSearchResult_success() {
        Page<Article> articles = service.getSearchResult("Lorem Ipsum", 0);
        // There should only be 1 search result
        assertEquals(articles.getContent().size(), 1);
    }

    @Test
    public void testAddToZip_success() throws IOException {
        String path = "test.bin";
        // File content as bytes
        byte[] bytes = "Hello, World!".getBytes();
        byte[] result;

        // Adding bytes to zip
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(baos)) {
            ArticleService.addToZip(path, bytes, zos);
            result = baos.toByteArray();
        }

        // Validating zipped bytes
        try (ByteArrayInputStream bais = new ByteArrayInputStream(result);
                ZipInputStream zis = new ZipInputStream(bais)) {
            // Check that the entry name matches the path
            ZipEntry entry = zis.getNextEntry();
            assertNotNull(entry);
            assertEquals(path, entry.getName());

            // Check that the extracted content matches the original bytes
            byte[] extractedBytes = zis.readAllBytes();
            assertArrayEquals(bytes, extractedBytes);
        }
    }

    @Test
    public void testDownloadFile_success() throws Exception {
        // Test file Url (google logo)
        String fileUrl = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png";
        // Download the file content
        byte[] resultBytes = ArticleService.downloadFile(fileUrl);
        // Check that the content is not empty
        assertTrue(resultBytes.length > 0);
    }

    @Test
    public void testDownloadFile_malformedUrl() throws Exception {
        // Pass a URL that simulates a malformed url
        String fileUrl = "not a url ()()()";
        // Download the file content
        byte[] resultBytes = ArticleService.downloadFile(fileUrl);
        // Check that the content is not empty
        assertEquals(0, resultBytes.length);
    }

    @Test
    public void testDownloadFile_iOError() throws Exception {
        // Pass a URL that simulates being down or unreachable
        String fileUrl = "http://example.invalid";
        // Download the file content
        byte[] resultBytes = ArticleService.downloadFile(fileUrl);
        // Check that the content is not empty
        assertEquals(0, resultBytes.length);
    }

    @Test
    public void testWriteZipToStream_success() throws IOException {
        String testUrl = "https://www.google.com";
        byte[] result;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(baos)) {
            ArticleService.writeZipToStream(testUrl, zos);
            result = baos.toByteArray();
        }

        // Validating zipped bytes
        try (ByteArrayInputStream bais = new ByteArrayInputStream(result);
                ZipInputStream zis = new ZipInputStream(bais)) {
            // find article.html in the zip
            boolean found = false;

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();

                if (entryName.equals("article.html")) {
                    found = true;
                }
            }
            assertTrue(found, "article.html not found in zip");
        }
    }
}
