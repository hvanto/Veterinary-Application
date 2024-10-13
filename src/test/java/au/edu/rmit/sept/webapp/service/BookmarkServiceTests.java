package au.edu.rmit.sept.webapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.model.Bookmark;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.ArticleRepository;
import au.edu.rmit.sept.webapp.repository.BookmarkRepository;
import au.edu.rmit.sept.webapp.repository.UserRepository;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookmarkServiceTests {
    @Autowired
    private BookmarkService service;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private ArticleRepository articleRepository;

    User testUser1;
    User testUser2;

    Article article1;
    Article article2;

    @BeforeEach
    public void setUp() {
        // Use unique emails for each user to avoid conflicts
        String uniqueEmailUser1 = "john.doe-" + UUID.randomUUID() + "@example.com";
        String uniqueEmailUser2 = "john.doe2-" + UUID.randomUUID() + "@example.com";

        // Create users with unique emails
        testUser1 = userRepository.save(new User("John", "Doe", uniqueEmailUser1, "password123", "123456789"));
        testUser2 = userRepository.save(new User("John2", "Doe2", uniqueEmailUser2, "password223", "223456789"));

        // Create articles with unique data
        article1 = articleRepository.save(new Article("Lorem Ipsum", "https://somelink1.com", "Dolor amet",
                "Nullam Volutpat", new Date(), "https://imagelink.com"));
        article2 = new Article("Curabitur non justo", "https://somelink2.com", "Congue tristique",
                "Donec Maximus", new Date(), "https://imagelink.com");

        // Save a bookmark for testUser1
        bookmarkRepository.save(new Bookmark(testUser1, article1));
    }

    @Test
    public void testFindByUser_success() {
        Page<Bookmark> bookmarks = service.findByUser(testUser1, 0);
        // 1 bookmark should be returned
        assertEquals(1, bookmarks.getContent().size());
    }

    @Test
    public void testFindByUser_notFound() {
        Page<Bookmark> bookmarks = service.findByUser(testUser2, 0);
        // 0 bookmarks should be returned
        assertEquals(0, bookmarks.getContent().size());
    }

    @Test
    public void testFindByUser_negativePage() {
        Page<Bookmark> bookmarks = service.findByUser(testUser1, -1);
        // 0 bookmarks should be returned
        assertEquals(0, bookmarks.getContent().size());
    }

    @Test
    public void testAddBookmark_success() {
        Bookmark bookmark = service.addBookmark(testUser1, article2);
        // both article and bookmark are added to the database
        assertTrue(articleRepository.findById(article2.getId()).isPresent());
        assertTrue(bookmarkRepository.findById(bookmark.getId()).isPresent());
    }

    @Test
    public void testAddBookmark_existingArticle() {
        Long count = articleRepository.count();
        Bookmark bookmark = service.addBookmark(testUser2, article1);
        // article already exists, only bookmark is added to database
        assertEquals(count, articleRepository.count());
        assertTrue(bookmarkRepository.findById(bookmark.getId()).isPresent());
    }

    @Test
    public void testRemoveBookmark_success() {
        // method should return true
        assertTrue(service.removeBookmark(testUser1, article1));
    }

    @Test
    public void testRemoveBookmark_notPresent() {
        // Bookmark does not exist
        // method should return false
        assertFalse(service.removeBookmark(testUser2, article1));
    }
}