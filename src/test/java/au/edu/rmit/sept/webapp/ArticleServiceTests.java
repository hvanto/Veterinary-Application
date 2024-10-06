package au.edu.rmit.sept.webapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import au.edu.rmit.sept.webapp.service.ArticleService;

@SpringBootTest
public class ArticleServiceTests {
    
    @Autowired
    private ArticleService articleService;

    @Test
    public void testAlwaysPasses() {
        
    }
}
