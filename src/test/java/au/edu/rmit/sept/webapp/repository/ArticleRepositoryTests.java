package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ArticleRepositoryTests {

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        // Delete all articles from database
        articleRepository.deleteAll();

        // Add custom test data
        Article article1 = new Article("Lorem Ipsum", "https://somelink.com",
                                    "Dolor amet", "Nullam Volutpat",
                                        new Date(), "https://imagelink.com");
        Article article2 = new Article("Curabitur non justo", "https://somelink.com",
                                    "Congue tristique", "Donec Maximus",
                                        new Date(), "https://imagelink.com");

        articleRepository.save(article1);
        articleRepository.save(article2);
    }

    @Test
    public void testSearchArticlesByKeyword_TitleMatch() {
         Pageable pageable = PageRequest.of(0,10);

         Page<Article> searchResult1 = articleRepository.searchArticlesByKeyword("Curabitur", pageable);
         Page<Article> searchResult2 = articleRepository.searchArticlesByKeyword("abitur", pageable);

        assertEquals(1, searchResult1.getTotalElements());
        assertEquals("Curabitur non justo", searchResult1.getContent().get(0).getTitle());
        assertEquals(1, searchResult2.getTotalElements());
        assertEquals("Curabitur non justo", searchResult2.getContent().get(0).getTitle());

    }

    @Test
    public void testSearchArticlesByKeyword_DescriptionMatch() {
        Pageable pageable = PageRequest.of(0,10);

        Page<Article> searchResult1 = articleRepository.searchArticlesByKeyword("dolor amet", pageable);
        Page<Article> searchResult2 = articleRepository.searchArticlesByKeyword("doLoR a", pageable);

        assertEquals(1,searchResult1.getTotalElements());
        assertEquals("Dolor amet", searchResult1.getContent().get(0).getDescription());
        assertEquals(1,searchResult2.getTotalElements());
        assertEquals("Dolor amet", searchResult2.getContent().get(0).getDescription());
    }

    @Test
    public void testSearchArticlesByKeyword_AuthorMatch() {
        Pageable pageable = PageRequest.of(0,10);

        Page<Article> searchResult1 = articleRepository.searchArticlesByKeyword("Nullam", pageable);
        Page<Article> searchResult2 = articleRepository.searchArticlesByKeyword("am volut", pageable);

        assertEquals(1,searchResult1.getTotalElements());
        assertEquals("Nullam Volutpat",searchResult1.getContent().get(0).getAuthor());
        assertEquals(1,searchResult2.getTotalElements());
        assertEquals("Nullam Volutpat",searchResult2.getContent().get(0).getAuthor());
    }

    @Test
    public void testSearchArticlesByKeyword_NoMatch() {
        Pageable pageable = PageRequest.of(0,10);

        Page<Article> searchResult = articleRepository.searchArticlesByKeyword("asdfghjkl", pageable);

        assertEquals(0, searchResult.getTotalElements());
    }

    @Test
    public void testSearchArticlesByKeyword_LargeDataset() {
        Pageable pageable = PageRequest.of(0,10);

        // Generate 1000 random articles and store in database
        for (int i = 0; i < 1000; i++) {
            Article article = new Article(randomStringGenerator(40),
                                        "https://somelink.com",
                                            randomStringGenerator(150),
                                            randomStringGenerator(20),
                                            new Date(),
                                    "https://imagelink.com");

            articleRepository.save(article);
        }

        // Start timer
        long startTime = System.currentTimeMillis();

        // Perform search
        Page<Article> searchResult = articleRepository.searchArticlesByKeyword("Lorem Ipsum", pageable);

        // End timer
        long endTime = System.currentTimeMillis();

        // Calculate duration
        long duration = endTime - startTime;

        // Ensure that search takes less than one second
        assertTrue(duration < 1000, "Search took longer than 1 second: " + duration + "ms");
        assertEquals(1, searchResult.getTotalElements());

    }

    public static String randomStringGenerator(int length) {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ";
        final Random RANDOM = new Random();

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }

}
