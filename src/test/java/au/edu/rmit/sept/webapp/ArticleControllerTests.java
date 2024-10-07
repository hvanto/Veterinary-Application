package au.edu.rmit.sept.webapp;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.model.Bookmark;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.ArticleRepository;
import au.edu.rmit.sept.webapp.repository.BookmarkRepository;
import au.edu.rmit.sept.webapp.repository.UserRepository;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArticleControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserRepository userRepository;
        @Autowired
        private BookmarkRepository bookmarkRepository;
        @Autowired
        private ArticleRepository articleRepository;

        String testArticle = "{"
                        + "\"title\":\"Lorem Ipsum\","
                        + "\"link\":\"https://somelink.com\","
                        + "\"author\":\"Dolor amet\","
                        + "\"publishedDate\":\"2024-09-12\","
                        + "\"description\":\"Nullam Volutpat\","
                        + "\"imageUrl\":\"https://imagelink.com\""
                        + "}";

        User testUser1;
        User testUser2;

        @BeforeAll
        public void setup() {
                bookmarkRepository.deleteAll();
                articleRepository.deleteAll();
                userRepository.deleteAll();

                // Create test user
                testUser1 = userRepository
                                .save(new User("John", "Doe", "john.doe@example.com", "password123", "123456789"));
                testUser2 = userRepository
                                .save(new User("John2", "Doe2", "john.doe2@example.com", "password223", "223456789"));
        }

        @AfterEach
        public void clean() {
                bookmarkRepository.deleteAll();
                articleRepository.deleteAll();
        }

        @AfterAll
        public void teardown() {
                userRepository.deleteAll();
        }

        @Test
        public void testAddArticle_success() throws Exception {
                // perform mock post to add article
                MvcResult result = mockMvc.perform(post("/article/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testArticle))
                                .andExpect(status().isOk())
                                .andReturn();

                // Extract the response body, which contains the article ID
                Long id = Long.parseLong(result.getResponse().getContentAsString());

                // Test if article exists in repository
                assertTrue(articleRepository.findById(id).isPresent());
        }

        @Test
        public void testRemoveArticle_success() throws Exception {
                // save Article to repository
                Long id = articleRepository.save(new Article("Lorem Ipsum", "https://somelink.com", "Dolor amet",
                                "Nullam Volutpat", new Date(), "https://imagelink.com")).getId();

                // perform mock delete to remove article
                mockMvc.perform(delete("/article/remove")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", id + ""))
                                .andExpect(status().isOk());

                // Test if article has been deleted
                assertTrue(articleRepository.findById(id).isEmpty());
        }

        @Test
        public void testAddBookmark_success() throws Exception {
                // Add the bookmark
                MvcResult result = mockMvc.perform(post("/addBookmark")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testArticle)
                                .param("userId", testUser1.getId() + ""))
                                .andExpect(status().isOk())
                                .andReturn();

                // Extract the response body, which contains the bookmark ID
                Long bookmarkId = Long.parseLong(result.getResponse().getContentAsString());
                Optional<Bookmark> bookmark = bookmarkRepository.findById(bookmarkId);

                // Test if bookmark has been added to repository
                assertTrue(bookmark.isPresent());

                Long articleId = bookmark.get().getArticle().getId();
                // Test if article has also been added to repository
                assertTrue(articleRepository.findById(articleId).isPresent());
        }

        @Test
        public void testAddBookmark_duplicate() throws Exception {
                // Add bookmark for testUser1
                MvcResult result1 = mockMvc.perform(post("/addBookmark")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testArticle)
                                .param("userId", testUser1.getId() + ""))
                                .andExpect(status().isOk())
                                .andReturn();

                // Extract the bookmark ID for testUser1
                Long bookmarkId1 = Long.parseLong(result1.getResponse().getContentAsString());
                Optional<Bookmark> bookmark1 = bookmarkRepository.findById(bookmarkId1);

                // Assert that the bookmark for testUser1 has been added
                assertTrue(bookmark1.isPresent());

                // Add bookmark for testUser2 with the same article
                MvcResult result2 = mockMvc.perform(post("/addBookmark")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testArticle)
                                .param("userId", testUser2.getId() + ""))
                                .andExpect(status().isOk())
                                .andReturn();

                // Extract the bookmark ID for testUser2
                Long bookmarkId2 = Long.parseLong(result2.getResponse().getContentAsString());
                Optional<Bookmark> bookmark2 = bookmarkRepository.findById(bookmarkId2);

                // Assert that the bookmark for testUser2 has been added
                assertTrue(bookmark2.isPresent());

                // Check that both bookmarks are associated with the same article
                Long articleId1 = bookmark1.get().getArticle().getId();
                Long articleId2 = bookmark2.get().getArticle().getId();
                assertEquals(articleId1, articleId2);

                // Check that only one instance of the article is present in the repository
                assertEquals(1, articleRepository.count());
        }

        @Test
        public void testRemoveBookmark_success() throws Exception {
                // Create and save test article directly in the repository
                Article article = new Article("Lorem Ipsum", "https://somelink.com", "Dolor amet",
                                "Nullam Volutpat", new Date(), "https://imagelink.com");
                articleRepository.save(article);
                // Create and save a bookmark for testUser1 directly in the repository
                Bookmark bookmark = new Bookmark(testUser1, article);
                bookmarkRepository.save(bookmark);
                Long bookmarkId = bookmark.getId(); // Store the bookmark ID

                // Assert that the bookmark exists in the repository
                assertTrue(bookmarkRepository.findById(bookmarkId).isPresent());

                // Perform POST request to remove the bookmark
                mockMvc.perform(post("/removeBookmark")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testArticle)
                                .param("userId", testUser1.getId() + ""))
                                .andExpect(status().isOk());

                // Assert that the bookmark has been deleted
                assertFalse(bookmarkRepository.findById(bookmarkId).isPresent());
        }

        @Test
        public void testSearchArticle_success() throws Exception {
                Article article = new Article("Lorem Ipsum", "https://somelink.com", "Dolor amet",
                                "Nullam Volutpat", new Date(), "https://imagelink.com");
                articleRepository.save(article);

                // Check whether the returned webpage contains the article link
                mockMvc.perform(get("/article/search")
                                .param("keyword", "Lorem Ipsum")
                                .param("page", "0"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(containsString("https://somelink.com")));
        }

        @Test
        public void testSearchArticle_notFound() throws Exception {
                // Nothing should be found as articles database is empty
                mockMvc.perform(get("/article/search")
                                .param("keyword", "Lorem Ipsum")
                                .param("page", "0"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(containsString("No results found.")));
        }

        @Test
        public void testDownloadArticle_success() throws Exception {
                mockMvc.perform(get("/downloadArticle")
                                .param("link", "https://www.google.com"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType("application/zip")); // Check if response is a ZIP file
        }
}
