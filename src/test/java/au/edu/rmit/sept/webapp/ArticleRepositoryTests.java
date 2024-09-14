package au.edu.rmit.sept.webapp;

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
                                    "Dolor amet", "Nullam Volutpat", new Date(),
                                    "https://imagelink.com");
        Article article2 = new Article("Curabitur non justo", "https://somelink.com",
                                    "Congue tristique", "Donec Maximus", new Date(),
                                    "https://imagelink.com");

        articleRepository.save(article1);
        articleRepository.save(article2);
    }

    @Test
    public void testSearchArticlesByKeyword_TitleMatch() {
         Pageable pageable = PageRequest.of(0,10);

         Page<Article> searchResult1 = articleRepository.searchArticlesByKeyword("Curabitur", pageable);
         Page<Article> searchResult2 = articleRepository.searchArticlesByKeyword("abitur", pageable);

        assert(searchResult1.getTotalElements() == 1);
        assert(searchResult1.getContent().get(0).getTitle().equals("Curabitur non justo"));
        assert(searchResult2.getTotalElements() == 1);
        assert(searchResult2.getContent().get(0).getTitle().equals("Curabitur non justo"));

    }

    @Test
    public void testSearchArticlesByKeyword_DescriptionMatch() {
        Pageable pageable = PageRequest.of(0,10);

        Page<Article> searchResult1 = articleRepository.searchArticlesByKeyword("dolor amet", pageable);
        Page<Article> searchResult2 = articleRepository.searchArticlesByKeyword("doLoR a", pageable);

        assert(searchResult1.getTotalElements() == 1);
        assert(searchResult1.getContent().get(0).getDescription().equals("Dolor amet"));
        assert(searchResult2.getTotalElements() == 1);
        assert(searchResult2.getContent().get(0).getDescription().equals("Dolor amet"));
    }

    @Test
    public void testSearchArticlesByKeyword_AuthorMatch() {
        Pageable pageable = PageRequest.of(0,10);

        Page<Article> searchResult1 = articleRepository.searchArticlesByKeyword("Nullam", pageable);
        Page<Article> searchResult2 = articleRepository.searchArticlesByKeyword("am volut", pageable);

        assert(searchResult1.getTotalElements() == 1);
        assert(searchResult1.getContent().get(0).getAuthor().equals("Nullam Volutpat"));
        assert(searchResult2.getTotalElements() == 1);
        assert(searchResult2.getContent().get(0).getAuthor().equals("Nullam Volutpat"));
    }

    @Test
    public void testSearchArticlesByKeyword_NoMatch() {
        Pageable pageable = PageRequest.of(0,10);

        Page<Article> searchResult = articleRepository.searchArticlesByKeyword("asdfghjkl", pageable);

        assert(searchResult.getTotalElements() == 0);
    }

}
